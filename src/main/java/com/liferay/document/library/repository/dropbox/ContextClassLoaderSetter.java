package com.liferay.document.library.repository.dropbox;

import com.liferay.portal.kernel.util.ClassLoaderUtil;

import java.io.Closeable;

public class ContextClassLoaderSetter implements Closeable {

    public ContextClassLoaderSetter(ClassLoader classLoader) {
        _originalClassLoader = ClassLoaderUtil.getContextClassLoader();

        ClassLoaderUtil.setContextClassLoader(classLoader);
    }

    @Override
    public void close() {
        ClassLoaderUtil.setContextClassLoader(_originalClassLoader);
    }

    private final ClassLoader _originalClassLoader;
}
