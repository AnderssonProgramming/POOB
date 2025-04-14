/**
 * Clase para implementar la bùsqueda binaria
 * @author Escuela Colombiana de Ingenieria Julio Garavito
 * @version 1.0
 */

import java.lang.Math;
import java.util.*;

public class BusquedaBinaria
{
    private static int maxTam = 10;
    private static int maxInt = 20;
    private static int minInt = 0;
    private static int busquedas = 10;

    /**
     * Crea un arreglo aleatorio ordenado llamando a la funciòn arregloRandom
     * @return arregloOrdenar Array ordenado
     */

    private static int[] arregloRandomOrdenado() {
        int[] arregloAOrdenar = arregloRandom();
        Arrays.sort(arregloAOrdenar);
        return arregloAOrdenar;
    }
    /**
     * Crea un Arreglo aleatorio desordenado
     * @return arreglo Array con elementos aleatorios de forma desordenada
     */

    private static int[] arregloRandom() {
        int[] arreglo = new int [selectRandInt(1,maxTam)];
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = selectRandInt(minInt,maxInt);
        }
        return arreglo;
    }

    /**
     * Busca un elemento en el arreglo dado
     * @param arreglo ordenado en el cual se desea hacer la bùsqueda
     * @param izq indice izquierdo
     * @param der indice derecho
     * @param x numero a buscar
     * @return res retorna la posicion si se encuentra el elemento, de lo contario retorna -1
     */

    private static int busquedaBinaria (int[] arr, int izq, int der, int x) {
        int res = -1;
        if (der >= izq) {
            int mid = (izq + der) / 2;
            if (arr[mid] == x){
                res = mid;
            }
                else if (arr[mid] > x){
                    res = busquedaBinaria(arr,izq,mid-1,x);
            }
                else{
                    res = busquedaBinaria(arr,mid+1,der,x);
            }
        }
        return res;
    }
    /**
     * Calcula un numero aleatorio dado un intervalo cerrado
     * @param infLimit menor valor del intervalo
     * @param supLimit mayor valor del intervalo
     * @return numeroRand el numero aleatorio
     */

    private static int selectRandInt (int infLimit, int supLimit) {
        int numeroRand = infLimit + (int)(Math.random() * supLimit);
        return numeroRand;
    }

    /**
     * Funciòn principal
     */
    public static void main(String[] args){
        int[] sec = arregloRandomOrdenado();
        System.out.println("Arreglo generado de tamaño " + sec.length + " y es: " + Arrays.toString(sec));

        for (int i = 0; i < busquedas; i++) {
            int consulta = selectRandInt(minInt, maxInt);
            int resultado = busquedaBinaria(sec, 0, sec.length-1, consulta);
            if (resultado != -1) {
                System.out.println("Buscando " + consulta + " en el arreglo: Se encuentra en la posicion " + resultado);   
            } else {
                System.out.println("Buscando " + consulta + " en el arreglo: No encontrado");
            }
        }

    }
    

}