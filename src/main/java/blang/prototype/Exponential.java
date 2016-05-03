package blang.prototype;

import java.util.Arrays;
import java.util.Collection;

import blang.core.Model;
import blang.core.SupportFactor;
import blang.core.SupportFactor.Support;
import blang.factors.Factor;
import blang.factors.LogScaleFactor;



public class Exponential implements LogScaleFactor, Model
{
  
  /*
   * Example of what would be generated by the DSL below. 
   * 
   
   model { // model keyword is used to generate class signature automatically; explicit type decl also accepted
   
     // emit signals what is in the LHS of A | B ~ C statements (via generated constructor)
     // also states what gets generated (via annotations)
     emit Real realization
   
     param double rate() 
     // question: maybe we can allow skipping ()? if possible would get:
     // param double rate
     
     support {
       realization.get() >= 0.0  
       // get rid of get() via overloading? if possible would get:
       // realization >= 0.0
     }
     
     logDensity {
       log(rate) - param.rate() * realization.get() 
       // get rid of of 'param.' by generating private double rate() { return param.rate() }, 
       // then since 
     }
     
     simulate(Random random) {
       val sample = ...
       // either return that value if we deal with primitives, or do it with change in place instead
     }
   
   }
   
   
   */
  
  public static interface ExponentialParams
  {
    public double getRate();
  }
  
  public final ExponentialParams params;
  public final Real realization;
  
  public Exponential(Real realization, ExponentialParams params){
    this.params = params;
    this.realization = realization;
    this.supportFactor = new SupportFactor(new ExponentialSupport(realization));
  }
  
  public final SupportFactor supportFactor;
  
  private static class ExponentialSupport implements Support
  {
    private final Real realization;
    
    private ExponentialSupport(Real realization)
    {
      this.realization = realization;
    }

    @Override
    public boolean isInSupport()
    {
      return realization.get() >= 0.0;
    }
  }

  @Override
  public double logDensity()
  {
    double x = realization.get();
    double rate = params.getRate();
    return Math.log(rate) - rate * x;
  }

  @Override
  public Collection<Factor> components()
  {
    return Arrays.asList(supportFactor);
  }
}
