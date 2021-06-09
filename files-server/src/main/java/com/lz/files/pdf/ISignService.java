package com.lz.files.pdf;

import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface ISignService {

    void executeAsyncSign(int i);
    void executeAsyncSign2( int i) throws IOException, DocumentException;

}
