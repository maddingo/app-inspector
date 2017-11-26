package no.maddin.inspect;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.reflect.Modifier.isStatic;

public class InstanceInspector {

    private static final Predicate<Field> IS_STATIC_FIELD = f -> isStatic(f.getModifiers());
    private static final Predicate<Field> IS_NON_PRIMITIVE = f -> !f.getType().isPrimitive();

    private final Pattern includePattern;
    private final FieldVisitor onVisit;
    private final Class[] classList;
    private final ExecutorService executor;
    private final Runnable stopCommand;

    public InstanceInspector(Class[] classList, FieldVisitor onVisit, Runnable stopCommand, String includes) {
        this.classList = classList;
        this.onVisit = onVisit;
        this.includePattern = Pattern.compile(includes);
        executor = Executors.newSingleThreadExecutor();
        this.stopCommand = stopCommand;
    }

    public void start() {
        executor.submit(this::processClasses);
    }

    public void stopAndWait(int seconds) throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(seconds, TimeUnit.MINUTES);
    }

    private void processClasses() {
        for (Class<?> cls : classList) {
            try {
                processFields(cls, onVisit, includePattern);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        stopCommand.run();
    }

    private void processFields(Object owner, FieldVisitor onVisit, Pattern include) throws ReflectiveOperationException {

        if (owner == null) {
            return;
        }
        java.util.function.Predicate<Field> fieldSelector;
        Class<?> ownerClass;
        if (owner instanceof Class) {
            if (Object.class.equals(owner)) {
                return;
            }
            fieldSelector = IS_STATIC_FIELD;
            ownerClass = (Class<?>) owner;
        } else {
            fieldSelector = IS_STATIC_FIELD.negate();
            ownerClass = owner.getClass();
        }
        if (!include.matcher(ownerClass.getName()).matches()) {
            return;
        }
        for (Field f : ownerClass.getDeclaredFields()) {
            if (fieldSelector.and(IS_NON_PRIMITIVE).test(f)) {
                f.setAccessible(true);
                Object obj = f.get(owner);
                if (obj != null) {
                    onVisit.onVisit(owner, f, obj);
                    // static fields are already handled from the loaded classes
                    processFields(obj, onVisit, include);
                }
            }
        }
    }

    @FunctionalInterface
    public interface FieldVisitor {
        void onVisit(Object owner, Field field, Object value);
    }
}
