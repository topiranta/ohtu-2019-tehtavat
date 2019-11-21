package ohtu.verkkokauppa;

import org.junit.Before;
import org.junit.Test;

import ohtu.verkkokauppa.Kauppa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KauppaTest {

    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {
        // luodaan ensin mock-oliot
        Pankki pankki = mock(Pankki.class);

        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        // määritellään että viitegeneraattori palauttaa viitten 42
        when(viite.uusi()).thenReturn(42);

        Varasto varasto = mock(Varasto.class);
        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        // sitten testattava kauppa 
        Kauppa k = new Kauppa(varasto, pankki, viite);              

        // tehdään ostokset
        k.aloitaAsiointi();
        k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        k.tilimaksu("pekka", "12345");

        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(),anyInt());   
        // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
    }

    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaArvoilla() {
        // luodaan ensin mock-oliot
        Pankki pankki = mock(Pankki.class);

        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        // määritellään että viitegeneraattori palauttaa viitten 42
        when(viite.uusi()).thenReturn(42);

        Varasto varasto = mock(Varasto.class);
        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        // sitten testattava kauppa 
        Kauppa k = new Kauppa(varasto, pankki, viite);              

        // tehdään ostokset
        k.aloitaAsiointi();
        k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        k.tilimaksu("pekka", "12345");

        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu oikeilla arvoilla
        verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);   

    }

    @Test
    public void kahtaTuotettaOstaessaKutsutaanPankinTilisiirtoaOikeillaArvoilla() {

        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);

        when(viite.uusi()).thenReturn(313);

        when(varasto.saldo(1)).thenReturn(7);
        when(varasto.saldo(2)).thenReturn(8);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "kriisiviesintakonsultaatio", 1000));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "logoJaBrandiUudistus", 2500));

        Kauppa puoti = new Kauppa(varasto, pankki, viite);

        puoti.aloitaAsiointi();
        puoti.lisaaKoriin(1);
        puoti.lisaaKoriin(2);
        puoti.tilimaksu("korporaatio", "FI13 2233 1141 04");

        verify(pankki).tilisiirto("korporaatio", 313, "FI13 2233 1141 04", "33333-44455", 3500);



    }

    @Test
    public void yhtaTuotettaKahtaKappalettaOstaessaKutsutaanPankinTilisiirtoaOikeillaArvoilla() {

        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);

        when(viite.uusi()).thenReturn(313);

        when(varasto.saldo(1)).thenReturn(7);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "kriisiviesintakonsultaatio", 1000));

        Kauppa puoti = new Kauppa(varasto, pankki, viite);

        puoti.aloitaAsiointi();
        puoti.lisaaKoriin(1);
        puoti.lisaaKoriin(1);
        puoti.tilimaksu("korporaatio", "FI13 2233 1141 04");

        verify(pankki).tilisiirto("korporaatio", 313, "FI13 2233 1141 04", "33333-44455", 2000);



    }

    @Test
    public void loppunuttaTuotettaOstaessaKutsutaanPankinTilisiirtoaOikeillaArvoilla() {

        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        Varasto varasto = mock(Varasto.class);

        when(viite.uusi()).thenReturn(313);

        when(varasto.saldo(1)).thenReturn(7);
        when(varasto.saldo(2)).thenReturn(0);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "kriisiviesintakonsultaatio", 1000));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "logoJaBrandiUudistus", 2500));

        Kauppa puoti = new Kauppa(varasto, pankki, viite);

        puoti.aloitaAsiointi();
        puoti.lisaaKoriin(1);
        puoti.lisaaKoriin(2);
        puoti.tilimaksu("korporaatio", "FI13 2233 1141 04");

        verify(pankki).tilisiirto("korporaatio", 313, "FI13 2233 1141 04", "33333-44455", 1000);



    }
}