import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.*;

/**
 Get sorted list of private fields the object declares (inherited fields should be skipped).
 */
class FieldGetter {

    public List<String> getPrivateFields(Object object) {
        // Add implementation here
        Field[] fields = object.getClass().getDeclaredFields();
        List<String> res = new ArrayList<>();
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                res.add(field.getName());
            }
        }

        Collections.sort(res);

        return res;
    }

}