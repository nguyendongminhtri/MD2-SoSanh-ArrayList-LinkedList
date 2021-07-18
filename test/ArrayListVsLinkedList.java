

import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArrayListVsLinkedList {
    private static final int MAX_ELEMENTS = 5;
    private static final int WARMUP_RUNS = 10000;
    private static final int BENCHMARK_RUNS = 100000;
    String[] strings = maxArray();

    // Does a JIT warmup run and then a benchmark averaged over many runs
    abstract class Benchmarkable {
        List<String> stringList = Arrays.asList(strings);
        List<String> testList;

        abstract String getName();
        abstract void setup();
        abstract void runMethod();

        public void doBenchMark() {
            int warmupRuns = WARMUP_RUNS;
            int benchmarkRuns = BENCHMARK_RUNS;


            for(int i=0; i<warmupRuns; i++){
                setup();
                runMethod();
            }

            // Timing loop
            long totalTime = 0;
            for(int i=0; i<benchmarkRuns; i++) {
                setup();
                long startTime = System.nanoTime();
                runMethod();
                long endTime = System.nanoTime();
                totalTime += (endTime-startTime);
            }
            System.out.println("Benchmark \""+getName()+"\" took "+totalTime/benchmarkRuns+" ns/run");
        }
    }

    ////////////// ADD ALL ////////////////////////////////////////
    @Test
    public void listAddAll() {

        Benchmarkable arrayListAddBenchmark = new Benchmarkable() {
            @Override
            public String getName() { return "Array List addAll()"; }

            @Override
            void setup() { testList = new ArrayList<>(); }

            @Override
            void runMethod() { testList.addAll(stringList); }
        };
        arrayListAddBenchmark.doBenchMark();

        Benchmarkable linkedListAddBenchmark = new Benchmarkable() {
            @Override
            public String getName() { return "Linked List addAll()"; }

            @Override
            void setup() { testList = new LinkedList<>(); }

            @Override
            void runMethod() { testList.addAll(stringList); }
        };
        linkedListAddBenchmark.doBenchMark();
    }

    @Test
    public void listAppend() {
        Benchmarkable arrayListAddBenchmark = new Benchmarkable() {
            @Override
            public String getName() { return "Array List add() from scratch"; }

            @Override
            void setup() { testList = new ArrayList<>(); }

            @Override
            void runMethod() {
                List<String> myList = testList;
                for (String string : strings)
                    testList.add(string);

            }
        };
        arrayListAddBenchmark.doBenchMark();

        Benchmarkable linkedListAddBenchmark = new Benchmarkable() {
            @Override
            public String getName() { return "Linked List add() from scratch"; }

            @Override
            void setup() { testList = new LinkedList<>(); }

            @Override
            void runMethod() {
                List<String> myList = testList;
                for (String string : strings)
                    testList.add(string);

            }
        };
        linkedListAddBenchmark.doBenchMark();
    }

    @Test
    public void testAppendLarge() {
        Benchmarkable arrayListBenchmark = new Benchmarkable() {
            String insertString0 = getString(true, MAX_ELEMENTS / 2 + 10);
            String insertString1 = getString(true, MAX_ELEMENTS / 2 + 20);
            String insertString2 = getString(true, MAX_ELEMENTS / 2 + 30);
            String insertString3 = getString(true, MAX_ELEMENTS / 2 + 40);

            @Override
            public String getName() { return "Array List add() on end"; }

            @Override
            void setup() {
                testList = new ArrayList<String>();

                // This is the most common way to add elements
                // Important because if an addAll operation increases
                // The backing array by more than 50% over existing to hold new elements
                // Then the backing array is sized *exactly* to hold all the elements.

                // Which means the next append operation would force a resize
                // This causes a large performance hit, since it would normally be amortized

                for (String s: stringList) {
                    testList.add(s);
                }
            }

            @Override
            void runMethod() {
                testList.add(insertString0);
                testList.add(insertString1);
                testList.add(insertString2);
                testList.add(insertString3);
            }
        };
        arrayListBenchmark.doBenchMark();

        Benchmarkable linkedListBenchmark = new Benchmarkable() {
            String insertString0 = getString(true, MAX_ELEMENTS / 2 + 10);
            String insertString1 = getString(true, MAX_ELEMENTS / 2 + 20);
            String insertString2 = getString(true, MAX_ELEMENTS / 2 + 30);
            String insertString3 = getString(true, MAX_ELEMENTS / 2 + 40);

            @Override
            public String getName() { return "Linked List add() on end"; }

            @Override
            void setup() {
                testList = new LinkedList<String>();

                // To replicate the arraylist setup
                for (String s: stringList) {
                    testList.add(s);
                }
            }

            @Override
            void runMethod() {
                testList.add(insertString0);
                testList.add(insertString1);
                testList.add(insertString2);
                testList.add(insertString3);
            }
        };
        linkedListBenchmark.doBenchMark();
    }

    @Test
    public void searchAndRemove() throws Exception {

        Benchmarkable arrayListBenchmark = new Benchmarkable() {
            String searchString0 = getString(true, MAX_ELEMENTS / 2 + 10);
            String searchString1 = getString(true, MAX_ELEMENTS / 2 + 20);

            @Override
            public String getName() { return "Array List search and remove"; }

            @Override
            void setup() {
                testList = new ArrayList<String>(MAX_ELEMENTS);
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> myList = testList;
                myList.remove(searchString0);
                myList.remove(searchString1);
            }
        };
        arrayListBenchmark.doBenchMark();

        Benchmarkable linkedListBenchmark = new Benchmarkable() {
            String searchString0 = getString(true, MAX_ELEMENTS / 2 + 10);
            String searchString1 = getString(true, MAX_ELEMENTS / 2 + 20);

            @Override
            public String getName() { return "Linked List search and remove"; }

            @Override
            void setup() {
                testList = new LinkedList<String>();
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> myList = testList;
                myList.remove(searchString0);
                myList.remove(searchString1);
            }
        };
        linkedListBenchmark.doBenchMark();
    }

    @Test
    public void search() throws Exception {

        Benchmarkable arrayListBenchmark = new Benchmarkable() {
            String searchString0 = getString(true, MAX_ELEMENTS / 2 + 10);
            String searchString1 = getString(true, MAX_ELEMENTS / 2 + 20);

            @Override
            public String getName() { return "Array List search"; }

            @Override
            void setup() {
                testList = new ArrayList<String>(MAX_ELEMENTS);
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> myList = testList;
                myList.contains(searchString0);
                myList.contains(searchString1);
            }
        };
        arrayListBenchmark.doBenchMark();

        Benchmarkable linkedListBenchmark = new Benchmarkable() {
            String searchString0 = getString(true, MAX_ELEMENTS / 2 + 10);
            String searchString1 = getString(true, MAX_ELEMENTS / 2 + 20);

            @Override
            public String getName() { return "Linked List search"; }

            @Override
            void setup() {
                testList = new LinkedList<String>();
                testList.addAll(stringList);
            }

            @Override
            void runMethod() {
                List<String> myList = testList;
                myList.contains(searchString0);
                myList.contains(searchString1);
            }
        };
        linkedListBenchmark.doBenchMark();
    }

    private String[] maxArray() {
        String[] strings = new String[MAX_ELEMENTS];
        Boolean result = Boolean.TRUE;
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            strings[i] = getString(result, i);
            result = !result;
        }
        return strings;
    }

    private String getString(Boolean result, int i) {
        return String.valueOf(result) + i + String.valueOf(!result);
    }
}
