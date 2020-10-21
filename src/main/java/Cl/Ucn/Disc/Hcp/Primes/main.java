package Cl.Ucn.Disc.Hcp.Primes;

import checkers.units.quals.A;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.chrono.IsoChronology;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main
 *
 * 1. Escribir una funcion que retorne true o false si un numero es primo
 * 2. Contar la cantidad de numeros primos entre el 2 y el 100.000.000 (se uso 1.000.000)
 * 3. Escribir un codigo que resuelva el punto 2, utilizando 1 a 16 hilos
 *
 * @author Franz Cortez
 */
public class main {

    private static final Logger log = LoggerFactory.getLogger(main.class);

    /**
     * verifica que es primo
     *
     * @param n the number to test
     * @return true is n is prime.
     */
    public static boolean isPrime(final long n){

        if(n<=0){
            throw new IllegalArgumentException("Error n no es primo");
        }

        if(n == 1){
            return false;
        }

        //TODO: REVISAR
        for(long i = 2; i < n; i++){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     *  MAIN
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

            int nHilos = 16;

            log.debug("INICIANDO TAREA CON {} HILO", nHilos);

            //cronometro
            final StopWatch watch =  StopWatch.createStarted();
            long start = System.currentTimeMillis();

            //Creacion de paquetes de hilos
            final ExecutorService executorService = Executors.newFixedThreadPool(nHilos);

            /*
            334455ms 1 hilo
            174221ms 2 hilos
            119004ms 3 hilos
            91863ms  4 hilos
            90790ms  5 hilos
            88711ms  6 hilos
            88775ms  7 hilos
            90480ms  8 hilos
            88725ms  9 hilos
            90555ms 10 hilos
            90548ms 11 hilos
            90746ms 12 hilos
            88712ms 13 hilos
            88813ms 14 hilos
            88824ms 15 hilos
            88731ms 16 hilos
             */
            for(long x = 2; x <= 1000000; x++){
                executorService.submit(new Tarea(x));
            }
            executorService.shutdown();

            executorService.awaitTermination(1, TimeUnit.HOURS);

            long time = System.currentTimeMillis() - start;

            log.debug("hay {} primos en {}ms con {} hilos",Tarea.getCont(),time,nHilos);

    }

    private static class Tarea implements Runnable {

        private long numero ;
        private static final AtomicInteger cont = new AtomicInteger(0);

        Tarea(long numero){
            this.numero = numero;
        }

        public static int getCont() {
            return cont.get();
        }

        @Override
        public void run() {
            if(isPrime(this.numero)){
                cont.incrementAndGet();
            }
        }
    }
}

