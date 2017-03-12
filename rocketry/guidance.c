#include "equations.h"
#include "../pid/pid_lib.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

int main(int argc, char** argv)
{
    clock_t start = clock();

    int noWait = 0;
    int noPrint = 0;
    int compact = 0;
    int graphical = 0;

    double yb = 3100, vb = 210, vmin = 2, kl = 0.005, kh = 0.012, m = 5, target = 3800;
    double dt = 0.05;
    double P = 1, I = 0, D = 0;

    /*
     * this god-awful for loop processes all the possible arguments for
     * command line invokation
     */

    for (int i = 1; i < argc; i++)
    {
        if (!strcmp(argv[i], "yb"))
        {
            sscanf(argv[i+1], "%lf", &yb);
        }
        else if (!strcmp(argv[i], "vb"))
        {
            sscanf(argv[i+1], "%lf", &vb);
        }
        else if (!strcmp(argv[i], "vmin"))
        {
            sscanf(argv[i+1], "%lf", &vmin);
        }
        else if (!strcmp(argv[i], "kl"))
        {
            sscanf(argv[i+1], "%lf", &kl);
        }
        else if (!strcmp(argv[i], "kh"))
        {
            sscanf(argv[i+1], "%lf", &kh);
        }
        else if (!strcmp(argv[i], "m"))
        {
            sscanf(argv[i+1], "%lf", &m);
        }
        else if (!strcmp(argv[i], "target"))
        {
            sscanf(argv[i+1], "%lf", &target);
        }
        else if (!strcmp(argv[i], "dt"))
        {
            sscanf(argv[i+1], "%lf", &dt);
        }
        else if (!strcmp(argv[i], "fast"))
        {
            noWait = 1;
        }
        else if (!strcmp(argv[i], "noprint"))
        {
            noPrint = 1;
            noWait = 1;
        }
        else if (!strcmp(argv[i], "compact"))
        {
            compact = 1;
        }
        else if (!strcmp(argv[i], "tune"))
        {
            i++;
            sscanf(argv[i], "%lf", &P);
            i++;
            sscanf(argv[i], "%lf", &I);
            i++;
            sscanf(argv[i], "%lf", &D);
        }
        else if (!strcmp(argv[i], "graphical"))
        {
            graphical = 1;
        }
    }

    /*
     * initialize the step velocity to burnout velocity, the step height to burnout height,
     * and the step drag gain to the passive drag gain.
     */
    double vi = vb;
    double yi = yb;
    double ki = kl;

    /*
     * calculate the range of achievable target altitudes given
     * the vehicle's current trajectory and characteristics
     */
    double ld_ta = t_a(vb, m, kl);
    double ld_alt = alt(yb, vb, m, kl, ld_ta);
    double hd_ta = t_a(vb, m, kh);
    double hd_alt = alt(yb, vb, m, kh, hd_ta);

    /*
     * header information is printed here. this includes target, burnout height, burnout
     * velocity, low drag constant, high drag constant, vehicle mass, low-drag altitude (100p),
     * high-drag altitude (100a), the simulation timestep, and the PID tuning gains.
     */

    if (!noPrint)
    {
        printf("Burnout.\n");
        printf("target:\t%d meters\r\t\t\t\t100p:\t%d meters\n", (int) target, (int)ld_alt);
        printf("yb:\t%d meters\r\t\t\t\t100a:\t%d meters\n", (int) yb, (int) hd_alt); 
        printf("vb:\t%g m/s\r\t\t\t\tdt:\t%g sec\n", vb, dt);
        printf("kl:\t%g kg/m\n", kl);
        printf("kh:\t%g kg/m\r\t\t\t\tPID:\t%g %g %g\n", kh, P, I, D); 
        printf("m:\t%g kg\n", m);

        if (hd_alt > target)
            printf("WARNING: WON'T REACH TARGET (TOO HIGH).");
        else if (ld_alt < target)
            printf("WARNING: WON'T REACH TARGET (TOO LOW).");
        else
            printf("Target can be reached.");
        printf(" Beginning guidance.\n- - - - - - - - - -\n");
    
        if (!graphical)
        {
            printf("(All values are displayed in SI units.)\n");
            printf("Time\t\tDest. Alt.\tFlap State\tAltitude\tVelocity\tAcceleration\n");
        }
        else
        {
            printf("(Convergence graphically displayed as horizontal bar.)\n");
            printf("Time\tDest. Alt.\tVelocity\tError\t- v +\n");
        }
    }
    
    double t = 0; // keeps track of time since burnout
    double max_error = 0; // for graphical display. computationally unnecessary
    Controller flaps = pid_init(P,I,D,-1);
    if (!noWait) wait(2);
    
    /*
     * updates every dt seconds (as many times per second as possible, ideally)
     * and runs calculations to attenuate the flaps and reach apogee, until
     * velocity reaches some threshold minimum (apogee is reached).
     */
    while(vi > vmin)
    {
        /*
         * at this point in the loop the experimentally measured values
         * should be determined. for the algorithm to work, only instantaneous
         * altitude and velocity must be known, within a reasonable degree of
         * certainty. if more time must be taken to minimize the variance of the
         * measurements, that's fine -- this is fairly robust to low polling
         * rates, though I think at least 5 Hz is necessary
         */
         
        // TODO: calculate real vi (step velocity) and yi (step altitude)
    
        /*
         * this block calculates the time to apogee and the height
         * of apogee if the rest of the flight is flown with passive
         * drag. it appraises the state of the trajectory to predict
         * if the target will be reached without further intervention.
         */
        double ta_passive = t_a(vi, m, kl);
        double ya_passive = alt(yi, vi, m, kl, ta_passive);

        /* computationally unnecessary. used for graphical display */
        if (ya_passive - target > max_error)
            max_error = ya_passive - target;
        
        /*
         * calculates the error between the predicted altitude
         * and the target; only checks for positive error, since
         * there's nothing we can do about negative error and it
         * really shouldn't ever happen
         */ 
        double error;
        if (ya_passive > target)
            error = -pid_seek(&flaps, ya_passive, target, dt);
        else
            error = 0;
        
        /*
         * decides to engage the flaps or not.
         * if error is above a certain threshold, engage flaps.
         */
        double ya_passive_next = 0;
        int flap_state = 0;
        if (error > 1)
        {
            /*
             * calculates the next step's predicted altitude to confirm
             * that high drag should be chosen for this step.
             * this is to avoid overshooting, since more drag
             * can always be applied later.
             */
            
            double yi_next = alt(yi, vi, m, kh, dt);
            double vi_next = vel(vi, m, kh, dt);
            double ta_passive_next = t_a(vi_next, m, kl);
            ya_passive_next = alt(yi_next, vi_next, m, kl, ta_passive_next);
            if (ya_passive_next > target)
            {
                ki = kh;
                flap_state = 1;
            }
            else
            {
                ki = kl;
                flap_state = 0;
            }
        }
        else
        {
            ki = kl;
            flap_state = 0;
        }

        /*
         * this block is unnecessary for the final algorithm - in this
         * simulation, the next step's instantaneous altitude and velocity
         * are calcuted using the ideal equations. in the field, they will
         * be measured experimentally.
         */
        yi = alt(yi, vi, m, ki, dt);
        vi = vel(vi, m, ki, dt);
        
        /* unnecessary to calculate acceleration but useful to know. */
        double ai = accel(vi, m, ki, dt);
        
        /* prints out useful data */
        if (!noPrint)
        {
            /* prints 6 columns of data if graphical is disabled */
            if (!graphical)
            {
                printf("\r%g", t);
                printf("\r\t\t%g", ya_passive);
                printf("\r\t\t\t\t%d    %d", flap_state, (int) error);
                printf("\r\t\t\t\t\t\t%g", yi);
                printf("\r\t\t\t\t\t\t\t\t%g", vi);
                printf("\r\t\t\t\t\t\t\t\t\t\t%g", ai);
                printf("\r\t\t\t\t\t\t\t\t\t\t\t\t%g", ya_passive_next);
            }
            /* prints 3 columns of data and an error bar if graphical is enabled */
            else
            {
                printf("%g", t);
                printf("\r\t%.4g", ya_passive);
                printf("\r\t\t\t%g\r\t\t\t\t\t", vi);
                for (int i = -8; i < 100; i++)
                {
                    int error = ya_passive - target;
                    if (i == 0)
                        printf(":");
                    else if (error > 0 && i > 0 && i < error * 50 / max_error)
                        printf("|");
                    else if (error < 0 && i < 0 && i > error * 50 / max_error)
                        printf("|");
                    else if (i < 0)
                        printf(" ");
                }
            }
            fflush(stdout);
        }
        if (!noWait) wait(dt*0.9); // wait dt seconds
        
        /* erases the old line if compact is enabled, starts a new line if not */
        if (!noPrint)
        {
            if (compact && vi > vmin)
            {
                printf("\r                                                    ");
                printf("                                                         \r");
            }
            if (!compact) printf("\n");
        }
        t += dt; // advance t by dt
    }

    double elapsed = ((double)(clock() - start))/CLOCKS_PER_SEC;
    if (compact && !noPrint) printf("\n");
    if (!noPrint) printf("- - - - - - - - - -\nApogee.\n");
    printf("Time elapsed: %g seconds (%g microseconds)\n", elapsed, 1000000*elapsed);
    if (!noPrint) printf("\n");

    return 0;
}