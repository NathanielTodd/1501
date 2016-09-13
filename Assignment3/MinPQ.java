import java.util.NoSuchElementException;

public class MinPQ{
	
    private int maxN;        // maximum number of elements on PQ
    private int N;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Car[] cars;      // Cars[i] = priority of i


    public MinPQ(int maxN) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        cars = new Car[maxN + 1];    // make this of length maxN??
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            qp[i] = -1;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public boolean contains(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        return qp[i] != -1;
    }

    public int size() {
        return N;
    }

    public void insertP(int i, Car car) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        N++;
        qp[i] = N;
        pq[N] = i;
        cars[i] = car;
        swimP(N);
    }
    public void insertM(int i, Car car) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        N++;
        qp[i] = N;
        pq[N] = i;
        cars[i] = car;
        swimM(N);
    }

    public int minIndex() {
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    public Car minCar() {
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        return cars[pq[1]];
    }

    public int delMinP() {
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];
        exch(1, N--);
        sinkP(1);
        qp[min] = -1;            // delete
        cars[pq[N + 1]] = null;    // to help with garbage collection
        pq[N + 1] = -1;            // not needed
        return min;
    }
    public int delMinM() {
        if (N == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];
        exch(1, N--);
        sinkM(1);
        qp[min] = -1;            // delete
        cars[pq[N + 1]] = null;    // to help with garbage collection
        pq[N + 1] = -1;            // not needed
        return min;
    }

    public Car carOf(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return cars[i];
    }

    public void changeCarP(int i, Car car) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        cars[i] = car;
        swimP(qp[i]);
        sinkP(qp[i]);
    }
    public void changeCarM(int i, Car car) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        cars[i] = car;
        swimM(qp[i]);
        sinkM(qp[i]);
    }

    public void deleteP(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, N--);
        swimP(index);
        sinkP(index);
        cars[i] = null;
        qp[i] = -1;
    }

    public void deleteM(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, N--);
        swimM(index);
        sinkM(index);
        cars[i] = null;
        qp[i] = -1;
    }

    /**iterating through the PQ in level order to find the highest priority car*/
    public void makeMinP(String make, String model){
        int nextLevel = 1;
        int car = 1;
        for(int j = 1;j<=N;j++){
            car = pq[j];
            for(;j<=nextLevel;++j){
                if(j>N){
                    break;
                }
                else if(cars[pq[j]].getMake().equals(make) && cars[pq[j]].getModel().equals(model)&&greaterP(car,j)){
                    car = pq[j];
                }
            }
            if(cars[car].getMake().equals(make) && cars[car].getModel().equals(model)){
                System.out.println(cars[car].toString());
                break;
            }
            nextLevel = (2*j)+1;
        }
    }

    /**iterating through the PQ in level order to find the highest priority car*/
    public void makeMinM(String make, String model){
        int nextLevel = 1;
        int car = 1;
        for(int j = 1;j<=N;j++){
            car = pq[j];
            for(;j<=nextLevel;++j){
                if(j>N){
                    break;
                }
                else if(cars[pq[j]].getMake().equals(make) && cars[pq[j]].getModel().equals(model)&&greaterM(car,j)){
                    car = pq[j];
                }
            }
            if(cars[car].getMake().equals(make) && cars[car].getModel().equals(model)){
                System.out.println(cars[car].toString());
                break;
            }
            nextLevel = (2*j)+1;
        }
    }

    /***************************************************************************
     * General helper functions.
     ***************************************************************************/
    private boolean greaterP(int i, int j) {
        return cars[pq[i]].getPrice() > (cars[pq[j]]).getPrice();
    }
    private boolean greaterM(int i, int j) {
        return cars[pq[i]].getMileage() > (cars[pq[j]]).getMileage();
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }


    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/
    private void swimP(int k) {
        while (k > 1 && greaterP(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }
    private void swimM(int k) {
        while (k > 1 && greaterM(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sinkP(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && greaterP(j, j + 1)) j++;
            if (!greaterP(k, j)) break;
            exch(k, j);
            k = j;
        }
    }
    private void sinkM(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && greaterM(j, j + 1)) j++;
            if (!greaterM(k, j)) break;
            exch(k, j);
            k = j;
        }
    }
	
}
