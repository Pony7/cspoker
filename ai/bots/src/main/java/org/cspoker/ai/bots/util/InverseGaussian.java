package org.cspoker.ai.bots.util;

import umontreal.iro.lecuyer.probdist.InverseGaussianDist;

public class InverseGaussian {
	
//	private double lambda = 0.02396;
//	private double mhu = 0.06056;
	
	private final double lambda;
	private final double mhu;
	
	public InverseGaussian(double lambda, double mhu) {
		this.lambda = lambda;
		this.mhu = mhu;
	}
	
	public double pdf(double x) {
		return Math.sqrt(lambda/(2*Math.PI*x*x*x))*
			Math.exp((-lambda*(x-mhu)*(x-mhu))/(2*mhu*mhu*x));
	}
	
	public double cdf(double x, double sigma) {
		return standardCdf(Math.sqrt(lambda/x)*((x/mhu)-1), sigma)+
				Math.exp(2*lambda/mhu)*standardCdf(-Math.sqrt(lambda/x)*((x/mhu)+1), sigma);
	}
	
	public double standardCdf(double x, double sigma) {
		return 0.5*(1+erf((x-mhu)/(sigma*Math.sqrt(2))));
	}
	
	private double erf(double x) {
		double a = 8*(Math.PI-3)/(3*Math.PI*(4-Math.PI));
		return Math.sqrt(1-Math.exp(-x*x*(((4/Math.PI)+a*x*x)/(1+a*x*x))));
	}
	
	public static void main(String[] args) {
		InverseGaussianDist i = new InverseGaussianDist(0.02396,0.0605638803471205);
		System.out.println(i.density(0.001));
		System.out.println(i.density(0.002));
		System.out.println(i.density(0.003));
		System.out.println(i.density(0.004));
		System.out.println();
		
		System.out.println(i.cdf(0.001));
		System.out.println(i.cdf(0.002));
		System.out.println(i.cdf(0.003));
		System.out.println(i.cdf(0.004));
		System.out.println();		
	}
}
