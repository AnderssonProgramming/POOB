from random import randint

MAX_TAM = 10
MAX_INT = 20
MIN_INT = 0
BUSQUEDAS = 10

def arreglo_random_ordenado():
    return sorted(arreglo_random())

def arreglo_random():
    return [randint(int(MIN_INT),int(MAX_INT)) for i in range(randint(1,MAX_TAM))]

def busqueda_binaria(arr, izq, der, x):
    res = -1

    if der >= izq:
        mid = (izq + der) // 2
        if arr[mid] == x:
            res = mid
        elif arr[mid] > x:
            res = busqueda_binaria(arr, izq, mid-1, x)
        else:
            res = busqueda_binaria(arr, mid+1, der, x)
    
    return res

def main():
    sec = arreglo_random_ordenado()
    print("Arreglo generado de tamaño",len(sec),"y es: ",sec)
    for buscar in range(BUSQUEDAS):
        consulta = randint(MIN_INT,MAX_INT)
        resultado = busqueda_binaria(sec, 0, len(sec) - 1,consulta)
        if resultado != -1:
            print("Buscando",consulta,"en el arreglo: Se encuentra en la posición",resultado)
        else:
            print("Buscando",consulta,"en el arreglo: No encontrado")

main()