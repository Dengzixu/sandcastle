package io.toolongname.sandcastle.component;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface StorageComponent {
    void save(InputStream stream, String objectKey);

    void save(byte[] bytes, String objectKey);

    void save(File file, String objectKey);

    String getPresignUrl(String objectKey);

    List<String> listAll();
}
