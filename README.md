# Libinjection - Java
This is a porting of the SQLi detection tool [libinjection](https://github.com/client9/libinjection) to Java.



```Java
public class Main {

    public static void main(String[] args) {
        /* test a string */
        Libinjection a = Libinjection.getInstance();
        boolean issqli = a.libinjection_sqli("admin' OR 1=1--");
        System.out.println(issqli);
    }
}
```

So far it's been tested on around 85,000 SQLi input collected from the original libinjection library [here](https://github.com/client9/libinjection/tree/master/data),
with results matching those of the original libinjection. Speed-wise similar to the original project (performance climbs as JVM optimizes).

This project was developed at [Qubit Security Inc.](http://en.qubitsec.com/)
