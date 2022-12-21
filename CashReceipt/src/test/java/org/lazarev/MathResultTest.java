package org.lazarev;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathResultTest {

    MathResult mathResult=new MathResult();

    @Test
    void sellOut() {

        assertEquals(7.13,mathResult.sellOut(1,1));
    }

    @Test
    void sellAtDiscount() {
        mathResult.setDiscount(0.89);
        assertEquals(6.3457,mathResult.sellAtDiscount(1,1));
    }

    @Test
    void returnSale() {
        assertEquals(0.88,mathResult.returnSale("1234"));
    }

    @Test
    void startWriterCheckFail() {
    }

    @Test
    void endWriterCheckFail() {
    }
}