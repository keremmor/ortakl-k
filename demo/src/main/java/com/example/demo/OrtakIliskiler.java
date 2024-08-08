package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

class Kisi {
    String ad;
    String soyad;
    String asilNumara;
    String ikincilNumara;
    String iliskiler; // İlişkiler string olarak saklanır

    public Kisi(String ad, String soyad, String asilNumara, String ikincilNumara) {
        this.ad = ad;
        this.soyad = soyad;
        this.asilNumara = asilNumara;
        this.ikincilNumara = ikincilNumara;
        this.iliskiler = "";
    }

    public void iliskiEkle(String ikincilNumara, String iliskiTipi) {
        iliskiler += ikincilNumara + ":" + iliskiTipi + ";";
    }

    public Set<String> getIliskilerIkincilNumaralar() {
        Set<String> numaralar = Arrays.stream(iliskiler.split(";"))
                .filter(s -> !s.isEmpty())
                .map(s -> s.split(":")[0])
                .collect(Collectors.toSet()) ;
        return numaralar;
    }

    public String getIliskiTipi(String ikincilNumara) {
        return Arrays.stream(iliskiler.split(";"))
                .filter(s -> !s.isEmpty())
                .filter(s -> s.startsWith(ikincilNumara + ":"))
                .map(s -> s.split(":")[1])
                .findFirst()
                .orElse(null);
    }
}

public class OrtakIliskiler {
    public static void main(String[] args) {
        // Kişilerin oluşturulması
        Kisi kisi201 = new Kisi("Ali", "Demir", "201", "301");
        Kisi kisi202 = new Kisi("Ayşe", "Çelik", "202", "302");
        Kisi kisi203 = new Kisi("Fatma", "Güneş", "203", "303");
        Kisi kisi204 = new Kisi("Mehmet", "Yıldız", "204", "304");
        Kisi kisi205 = new Kisi("Hasan", "Kara", "205", "305");

        // Kişileri tanımlama ve ilişkilerini ekleme
        Kisi kisi1 = new Kisi("Ahmet", "Yılmaz", "100", "200");
        kisi1.iliskiEkle(kisi201.ikincilNumara, "Arkadaşı");
        kisi1.iliskiEkle(kisi202.ikincilNumara, "Meslektaşı");
        kisi1.iliskiEkle(kisi203.ikincilNumara, "Kardeşi"); // Ortak iliskili kişi 1
        kisi1.iliskiEkle(kisi204.ikincilNumara, "Kuzeni"); // Ortak iliskili kişi 2

        Kisi kisi2 = new Kisi("Mehmet", "Kaya", "101", "201");
        kisi2.iliskiEkle(kisi203.ikincilNumara, "Komşusu"); // Ortak iliskili kişi 1
        kisi2.iliskiEkle(kisi204.ikincilNumara, "Arkadaşı"); // Ortak iliskili kişi 2
        kisi2.iliskiEkle(kisi205.ikincilNumara, "Amcası");

        Kisi kisi3 = new Kisi("Zeynep", "Kara", "102", "302");
        kisi3.iliskiEkle(kisi203.ikincilNumara, "Tanıdığı"); // Ortak iliskili kişi 1

        // Tüm kişileri tek bir listeye eklemek
        List<Kisi> tumKisiler = Arrays.asList(kisi1, kisi2, kisi3, kisi201, kisi202, kisi203, kisi204, kisi205);

        // Ortak ikincil numaraya sahip kişileri bulma
        Map<String, Set<Kisi>> ikincilNumaralarMap = new HashMap<>();

        // Her kişi için ilişkiler haritasını oluştur
        for (Kisi kisi : tumKisiler) {
            Set<String> ikincilNumaralar = kisi.getIliskilerIkincilNumaralar();
            for (String ikincilNumara : ikincilNumaralar) {
                ikincilNumaralarMap.computeIfAbsent(ikincilNumara, k -> new HashSet<>()).add(kisi);
            }
        }

        // Ortak ikincil numaraları bulma
        Set<String> ortakIkincilNumaralar = ikincilNumaralarMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (ortakIkincilNumaralar.isEmpty()) {
            System.out.println("Ortak ikincil numaraya sahip ilişki bulunamadı.");
        } else {
            System.out.println("Ortak ikincil numaraya sahip ilişkili kişiler:");
            for (String ikincilNumara : ortakIkincilNumaralar) {
                System.out.println("İkincil Numarası: " + ikincilNumara);
                Set<Kisi> ortakKisiler = ikincilNumaralarMap.get(ikincilNumara);

                Map<String, List<String>> kisiIliskileri = new HashMap<>();

                for (Kisi kisi : ortakKisiler) {
                    for (Kisi kisiDiğer : tumKisiler) {
                        if (kisiDiğer.getIliskilerIkincilNumaralar().contains(ikincilNumara)) {
                            String iliskiTipi = kisiDiğer.getIliskiTipi(ikincilNumara);
                            kisiIliskileri.computeIfAbsent(kisi.ad + " " + kisi.soyad, k -> new ArrayList<>())
                                    .add(kisiDiğer.asilNumara + " numaralı kişinin " + iliskiTipi);
                        }
                    }
                }

                for (Map.Entry<String, List<String>> entry : kisiIliskileri.entrySet()) {
                    String kisiAdSoyad = entry.getKey();
                    String iliskiler = String.join(", ", entry.getValue());
                    System.out.println(kisiAdSoyad + " - " + iliskiler);
                }
            }
        }
    }
}

