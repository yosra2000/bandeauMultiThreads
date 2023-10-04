package bandeau;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Classe utilitaire pour représenter la classe-association UML
 */
class ScenarioElement {

    Effect effect;
    int repeats;

    ScenarioElement(Effect e, int r) {
        effect = e;
        repeats = r;
    }
}
/**
 * Un scenario mémorise une liste d'effets, et le nombre de repetitions pour chaque effet
 * Un scenario sait se jouer sur un bandeau.
 */
public class Scenario {

    private final List<ScenarioElement> myElements = new LinkedList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();
    private class ScenarioThread extends Thread{
        //private final Bandeau b;
        private Bandeau b;

        public ScenarioThread(Bandeau b) {
            this.b = b;
        }
        public void run() {
            synchronized(b){
                for (ScenarioElement element : myElements) {
                    for (int repeats = 0; repeats < element.repeats; repeats++) {
                        element.effect.playOn(b);
                    }
                }
            }

        }
    }
    /**
     * Ajouter un effect au scenario.
     *
     * @param e l'effet à ajouter
     * @param repeats le nombre de répétitions pour cet effet
     */
    public void addEffect(Effect e, int repeats) {
        write.lock();
        try{
            myElements.add(new ScenarioElement(e, repeats));
        }finally {
            write.unlock();
        }

    }


    /**
     * Jouer ce scenario sur un bandeau
     *
     * @param b le bandeau ou s'afficher.
     */


        public void playOn(Bandeau b) {
            read.lock();
            try{
                ScenarioThread t = new ScenarioThread(b);
                t.start();
            }finally {
                read.unlock();
            }

        }

    }

