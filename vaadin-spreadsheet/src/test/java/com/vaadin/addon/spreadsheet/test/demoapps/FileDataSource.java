package com.vaadin.addon.spreadsheet.test.demoapps;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.server.data.ListDataSource;

/**
Helper class a replacement for Vaadin 7 FilesystemContainer
Doesn't support hierarchy.
 */
class FileDataSource {
    /**
     *
     * @param root root folder
     * @param filter Regexp for filtering files.
     * @return a ListDataSource of File
     */
    public static ListDataSource<File> create(URI root, String filter) {
        List<File> pathFiles = new ArrayList<>();

        try(Stream<Path> paths = Files.walk(Paths.get(root),1)) {
            paths.forEach(filePath -> {
                if(!Files.isDirectory(filePath) && (filePath.getFileName().toString().matches(filter))) {
                    pathFiles.add(filePath.toFile());
                }
            });
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ListDataSource<>(pathFiles);
    }

}
