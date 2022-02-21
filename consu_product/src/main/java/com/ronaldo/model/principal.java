//Consumidor = si hay pastel le resto 1 a la variable
//Consumidor = Si no hay despierto al cocinero y me duermo
//Cocinero = Me duermo esperando a que me llamen //Productor
// Si me llaman produzco 10 trozos de pastel y me duermo
package com.ronaldo.model;

public class principal implements Runnable {
    private boolean consumidor;

    private static int pastel = 0;
    private static final Object lock = new Object();

    public principal(boolean consumidor) {
        this.consumidor = consumidor;

    }
    // Bucle Infinito
    @Override
    public void run() {
        while (true) {
            if (consumidor) {
                consumiendo();
            } else {
                cocinando();
            }
        }
    }

    private void consumiendo() {
        synchronized (lock) {
            if (pastel > 0) {
                pastel--;
                System.out.println("Quedan " + pastel + " porciones de pastel");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        else{
            lock.notifyAll();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
    //Cocinero
    //========
    private void cocinando() {
        synchronized (lock){
            if (pastel == 0){
                pastel =10;
                System.out.println("=====================================");
                System.out.println("Soy el Chefcito y quedan "+pastel+".");
                System.out.println("=====================================");
                lock.notifyAll();
            }
            try{
                lock.wait();
            }catch (Exception ex){}

        }
    }

    public static  void main(String[] args){
        int numHilos = 11;

        Thread[] hilos = new Thread[numHilos];

        for (int i = 0 ; i < hilos.length; i++){
            Runnable runnable = null;

            if (i !=0) {
                runnable = new principal(true);
            }
            else {
                runnable = new principal(false);
            }

            hilos[i] = new Thread(runnable);
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (Exception ignored) {
            }
        }
    }

}
