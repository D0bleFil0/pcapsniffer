// PcapSniffer.java
package com.github.d0blefil0;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapDumper;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.core.PcapStat;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.util.NifSelector;

// Clase principal del programa
public class PcapSniffer {
    // Objeto time para usar en nombre fichero pcap
    static String time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new java.util.Date());                                                                                  // usar
                                                                                            
    // Getter para mostrar el dispositivo de red a usar
    static PcapNetworkInterface getNetworkDevice() {
        PcapNetworkInterface nif = null;
        // Crea el objeto nif con valor nullpara la seleccion de interfaz dentro de un
        // try-catch para capturar la excepcion
        try {
            nif = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nif;
    }

    // Metodo main
    public static void main(String[] args) throws PcapNativeException, NotOpenException {
        // Crea objeto nif usando constructor PcapNetworkInterface y lo inicializa con
        // valor del getter
        PcapNetworkInterface nif = getNetworkDevice();
        System.out.println("You chose: " + nif);

        // Condicional if para evitar valores nulos en seleccion menu
        if (nif == null) {
            System.out.println("No device chosen.");
            System.exit(1);
        }

        // Pcap handle, bloque para capturar los paquetes
        int snapLen = 65536; // tamaño de trama en bytes
        PromiscuousMode mode = PromiscuousMode.PROMISCUOUS; // modo promiscuo de la intefaz
        int timeout = 10; // tiempo en milisegundos para leer paquetes
        PcapHandle handle = nif.openLive(snapLen, mode, timeout); // objeto tipo handle para capturar paquetes con
                                                                  // metodo openLive
        PcapDumper dumper = handle.dumpOpen(time + ".pcap"); // objeto tipo dumper para guardar los paquetes capturados

        // Filtro para las cabeceras de los paquetes (ver wireshark)
         String filter = "tcp port 443";
        handle.setFilter(filter, BpfCompileMode.OPTIMIZE);

        // Objeto de tipo PacketListener para gestionar los paquetes recibidos
        PacketListener listener = new PacketListener() {

            public void gotPacket(PcapPacket packet) {
                // Imprime la informacion de los paquetes
                System.out.println(handle.getTimestampPrecision());
                System.out.println(packet);

                // Volcado de la informacion de los paquetes al fichero que se guardará
                try {
                    dumper.dump(packet);
                } catch (NotOpenException e) {
                    e.printStackTrace();
                }
            }
        };

        // Loop usando el listener, le indicamos la cantidad de paquetes que debe
        // recibir
        try {
            int maxPackets = 50;
            handle.loop(maxPackets, listener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Imprime por pantalla un resumen de los datos de los paquetes, usando un
        // objeto de tipo PcapStat
        PcapStat stats = handle.getStats();
        System.out.println("Packets received: " + stats.getNumPacketsReceived());
        System.out.println("Packets dropped: " + stats.getNumPacketsDropped());
        System.out.println("Packets dropped by interface: " + stats.getNumPacketsDroppedByIf());
        System.out.println("Packets captured: " + stats.getNumPacketsCaptured());

        // Metodo close para los objetos dumper y handle
        dumper.close();
        handle.close();
    }
}
