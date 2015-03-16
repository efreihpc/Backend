#ifndef M_PI
#define M_PI 3.14159265358979
#endif

kernel
    void MonteCarloKernel(
    global float * restrict vcall, // option call value (OUT)
    global float * restrict vput, // option put value (OUT)
    global const uint2 *randomseed,
    float r, // risk free (IN)
    float sigma, // volatility (IN)
    global const float *s_price, // current stock price (IN)
    global const float *k_price, // option strike price (IN)
    global const float *t // time (IN)
    )
{
    int tid = get_global_id(0);

    float tmp_bs1 = (r - sigma*sigma*0.5)*t[tid]; // formula reference: (r - (sigma^2)/2)*(T)
    float tmp_bs2 = sigma*sqrt(t[tid]); // formula reference: sigma * (T)^(1/2)

    // Initialize options price
    vcall[tid]   = (float)0.0;
    vput[tid]    = (float)0.0;

    int i = 0;

    for (int iSample = 0; iSample < NSAMP; iSample = iSample + 2)
    {
        uint seed = randomseed[tid].x + iSample % tid;
        uint t = seed ^ (seed << 11);
        uint rnd_src = randomseed[tid].y ^ (randomseed[tid].y >> 19) ^ (t ^ (t >> 8));
        float rnd_num = rnd_src*cos(2.0*M_PI*rnd_src);

        seed = randomseed[tid].x + tid % iSample;
        t = seed ^ (seed << 11);
        rnd_src = randomseed[tid].y ^ (randomseed[tid].y >> 19) ^ (t ^ (t >> 8));
        float rnd_num1 = rnd_src*sin(2.0*M_PI*rnd_src);

        float tmp_bs3 = rnd_num*tmp_bs2 + tmp_bs1;
        tmp_bs3 = s_price[tid]*exp(tmp_bs3);

        float dif_call = tmp_bs3-k_price[tid];


        vcall[tid] += max(dif_call, (float)0.0);

        tmp_bs3 = rnd_num1*tmp_bs2 + tmp_bs1;
        tmp_bs3 = s_price[tid]*exp(tmp_bs3);


        dif_call = tmp_bs3-k_price[tid];

        vcall[tid] += max(dif_call, (float)0.0);

    }

    // Average
    vcall[tid] = vcall[tid] / ((float)NSAMP) * exp(-r*t[tid]);


    // Calculate put option price from call option price: put = call – S0 + K * exp( -rT )
    vput[tid] = vcall[tid] - s_price[tid] + k_price[tid] * exp(-r*t[tid]);
}
