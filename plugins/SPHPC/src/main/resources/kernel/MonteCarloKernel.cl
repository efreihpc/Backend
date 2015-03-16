#ifdef __DO_FLOAT__
#define tfloat float
#else
#pragma OPENCL EXTENSION cl_khr_fp64 : enable
#define tfloat double
#endif

#ifndef M_PI
#define M_PI 3.14159265358979
#endif

kernel
    void MonteCarloKernel(
    global tfloat * restrict vcall, // option call value (OUT)
    global tfloat * restrict vput, // option put value (OUT)
    uint2 randomseed,
    tfloat r, // risk free (IN)
    tfloat sigma, // volatility (IN)
    global const tfloat *s_price, // current stock price (IN)
    global const tfloat *k_price, // option strike price (IN)
    global const tfloat *t // time (IN)
    )
{
    int tid = get_global_id(0);

    tfloat tmp_bs1 = (r - sigma*sigma*0.5)*t[tid]; // formula reference: (r - (sigma^2)/2)*(T)
    tfloat tmp_bs2 = sigma*sqrt(t[tid]); // formula reference: sigma * (T)^(1/2)

    // Initialize options price
    vcall[tid]   = (tfloat)0.0;
    vput[tid]    = (tfloat)0.0;

    int i = 0;

    for (int iSample = 0; iSample < NSAMP; iSample = iSample + 2)
    {
        uint seed = randoms.x + iSample % globalID;
        uint t = seed ^ (seed << 11);
        uint rnd_src = randoms.y ^ (randoms.y >> 19) ^ (t ^ (t >> 8));
        tfloat rnd_num = rnd_src*cos(2.0*M_PI*rnd_num1);

        seed = randoms.x + globalID % iSample;
        t = seed ^ (seed << 11);
        rnd_src = randoms.y ^ (randoms.y >> 19) ^ (t ^ (t >> 8));
        tfloat rnd_num1 = rnd_src*sin(2.0*M_PI*rnd_num1);

        tfloat tmp_bs3 = rnd_num*tmp_bs2 + tmp_bs1;
        tmp_bs3 = s_price[tid]*exp(tmp_bs3);

        tfloat dif_call = tmp_bs3-k_price[tid];


        vcall[tid] += max(dif_call, (tfloat)0.0);

        tmp_bs3 = rnd_num1*tmp_bs2 + tmp_bs1;
        tmp_bs3 = s_price[tid]*exp(tmp_bs3);


        dif_call = tmp_bs3-k_price[tid];

        vcall[tid] += max(dif_call, (tfloat)0.0);

    }

    // Average
    vcall[tid] = vcall[tid] / ((tfloat)NSAMP) * exp(-r*t[tid]);


    // Calculate put option price from call option price: put = call – S0 + K * exp( -rT )
    vput[tid] = vcall[tid] - s_price[tid] + k_price[tid] * exp(-r*t[tid]);
}
