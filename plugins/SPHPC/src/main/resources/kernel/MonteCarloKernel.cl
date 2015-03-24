#ifndef M_PI
#define M_PI 3.14159265358979
#endif

double2 boxMuller(double2 uniform)
{
    double r = sqrt(-2 * log(uniform.x));
    double theta = 2 * M_PI * uniform.y;
    return r * sin(theta), r * cos(theta);
}

uint rand_uint(uint2* rvec) {  //Adapted from http://cas.ee.ic.ac.uk/people/dt10/research/rngs-gpu-mwc64x.html
    #define A 4294883355U
    uint x=rvec->x, c=rvec->y; //Unpack the state
    uint res = x ^ c;          //Calculate the result
    uint hi = mul_hi(x,A);     //Step the RNG
    x = x*A + c;
    c = hi + (x<c);
    *rvec = (uint2)(x,c);      //Pack the state back up
    return res;                //Return the next result
    #undef A
}
inline double rand_double(uint2* rvec) {
    return (double)(rand_uint(rvec)) / (double)(0xFFFFFFFF);
}

kernel void
  MonteCarloKernel(   double drift,
                      double deviation,
                      double begin,
                      int numberOfDays,
                      global const uint *randomseedX,
                      global const uint *randomseedY,
                      global double *result)
  {
    int tid = get_global_id(0);
    uint2 randomSeed = (randomseedX[tid], randomseedY[tid]);
    int day = 0;

    for (int day = 0; day < numberOfDays; day = day + 2)
    {
        double rndX = rand_double(&randomSeed);
        double rndY = rand_double(&randomSeed);

        double2 normalizedRandom = boxMuller((rndX, rndY));

        double last;
        if(day == 0)
          last = begin;
        else
          last = result[day - 1];

        result[day] = last*exp(drift+deviation*normalizedRandom.x);
        result[day + 1] = result[day]*exp(drift+deviation*normalizedRandom.y);
    }
  }