import org.cspoker.ai.bots.util.Gaussian;

import junit.framework.TestCase;

/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

public class MaxGaussianTest extends TestCase {

	public void testEqual() throws Exception {
		Gaussian a = new Gaussian(0,1);
		Gaussian m = Gaussian.maxOf(a,a);
		System.out.println(m);
		assertTrue(close(m.mean, 0.5642, 0.01));
	}
	
	public void test3() throws Exception {
		Gaussian a = new Gaussian(0,1);
		Gaussian m = Gaussian.maxOf(a,a,a);
		System.out.println(m);
		assertTrue(close(m.mean, 0.846, 0.01));
	}

	
	public void test10() throws Exception {
		Gaussian a = new Gaussian(0,1);
		Gaussian m = Gaussian.maxOf(a,a,a,a,a,a,a,a,a,a);
		System.out.println(m);
		assertTrue(close(m.mean, 1.5388, 0.01));
	}

	public void testDifferent() throws Exception {
		Gaussian a = new Gaussian(0,1);
		Gaussian b = new Gaussian(-1000,1);

		Gaussian m = Gaussian.maxOf(a,b);
		System.out.println(m);
		assertTrue(close(m.mean, 0, 0.01));
		assertTrue(close(m.variance, 1, 0.01));
	}


	public void testDifferentStdDev() throws Exception {
		Gaussian a = new Gaussian(5,10);
		Gaussian b = new Gaussian(-1000,1);

		Gaussian m = Gaussian.maxOf(a,b);
		System.out.println(m);
		assertTrue(close(m.mean, 5, 0.01));
		assertTrue(close(m.variance, 10, 0.01));
	}

	
	public void testDifferentReverse() throws Exception {
		Gaussian a = new Gaussian(0,1);
		Gaussian b = new Gaussian(-1000,1);

		Gaussian m = Gaussian.maxOf(b,a);
		System.out.println(m);
		assertTrue(close(m.mean, 0, 0.01));
		assertTrue(close(m.variance, 1, 0.01));
	}
	
	private boolean close(double a, double b, double diff){
		return Math.abs(a-b)<diff;
	}
	
}
