import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 Get list of public fields the object declares (inherited fields should be skipped).
 */
class FieldGetter {

    public String[] getPublicFields(Object object) {
        // Add implementation here
        Field[] fields = object.getClass().getDeclaredFields();
        List<String> res = new ArrayList<>();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers())) {
                res.add(field.getName());
            }
        }


        return res.toArray(new String[0]);
    }

}