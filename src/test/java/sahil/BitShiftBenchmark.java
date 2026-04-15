package sahil;
import org.openjdk.jmh.annotations.*;
 
@State(Scope.Thread)
public class BitShiftBenchmark {
 
    long value = 1;
 
    @Benchmark
    public long multiply() {
        return value * 2;
    }
 
    @Benchmark
    public long bitshift() {
        return value << 1;
    }
}