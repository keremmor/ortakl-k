package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

class Kisi {
    String ad;
    String soyad;
    String asilTcKimlikNo;
    String iliskiliTcKimlikNo;
    String iliskiler; // İlişkiler string olarak saklanır

    public Kisi(String ad, String soyad, String asilTcKimlikNo, String iliskiliTcKimlikNo) {
        this.ad = ad;
        this.soyad = soyad;
        this.asilTcKimlikNo = asilTcKimlikNo;
        this.iliskiliTcKimlikNo = iliskiliTcKimlikNo;
        this.iliskiler = "";
    }

    public void iliskiEkle(String iliskiliTcKimlikNo, String iliskiTipi) {
        iliskiler += iliskiliTcKimlikNo + ":" + iliskiTipi + ";";
    }

    public Set<String> getIliskiliTcKimlikNolar() {
        return Arrays.stream(iliskiler.split(";"))
                .filter(s -> !s.isEmpty())
                .map(s -> s.split(":")[0])
                .collect(Collectors.toSet());
    }

    public String getIliskiTipi(String iliskiliTcKimlikNo) {
        return Arrays.stream(iliskiler.split(";"))
                .filter(s -> !s.isEmpty())
                .filter(s -> s.startsWith(iliskiliTcKimlikNo + ":"))
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
        kisi1.iliskiEkle(kisi201.iliskiliTcKimlikNo, "Arkadaşı");
        kisi1.iliskiEkle(kisi202.iliskiliTcKimlikNo, "Meslektaşı");
        kisi1.iliskiEkle(kisi203.iliskiliTcKimlikNo, "Kardeşi"); // Ortak ilişkili kişi 1
        kisi1.iliskiEkle(kisi204.iliskiliTcKimlikNo, "Kuzeni"); // Ortak ilişkili kişi 2

        Kisi kisi2 = new Kisi("Mehmet", "Kaya", "101", "201");
        kisi2.iliskiEkle(kisi203.iliskiliTcKimlikNo, "Komşusu"); // Ortak ilişkili kişi 1
        kisi2.iliskiEkle(kisi204.iliskiliTcKimlikNo, "Arkadaşı"); // Ortak ilişkili kişi 2
        kisi2.iliskiEkle(kisi205.iliskiliTcKimlikNo, "Amcası");

        Kisi kisi3 = new Kisi("Zeynep", "Kara", "102", "302");
        kisi3.iliskiEkle(kisi203.iliskiliTcKimlikNo, "Tanıdığı"); // Ortak ilişkili kişi 1

        // Tüm kişileri tek bir listeye eklemek
        List<Kisi> tumKisiler = Arrays.asList(kisi1, kisi2, kisi3, kisi201, kisi202, kisi203, kisi204, kisi205);

        // Ortak iliskiliTcKimlikNo'lara sahip kişileri bulma
        Map<String, Set<Kisi>> iliskiliTcKimlikNolarMap = new HashMap<>();

        // Her kişi için ilişkiler haritasını oluştur
        for (Kisi kisi : tumKisiler) {
            Set<String> iliskiliTcKimlikNolar = kisi.getIliskiliTcKimlikNolar();
            for (String iliskiliTcKimlikNo : iliskiliTcKimlikNolar) {
                iliskiliTcKimlikNolarMap.computeIfAbsent(iliskiliTcKimlikNo, k -> new HashSet<>()).add(kisi);
            }
        }

        // Ortak iliskiliTcKimlikNo'ları bulma
        Set<String> ortakIliskiliTcKimlikNolar = iliskiliTcKimlikNolarMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (ortakIliskiliTcKimlikNolar.isEmpty()) {
            System.out.println("Ortak iliskiliTcKimlikNo'ya sahip ilişki bulunamadı.");
        } else {
            System.out.println("Ortak iliskiliTcKimlikNo'ya sahip ilişkili kişiler:");
            for (String iliskiliTcKimlikNo : ortakIliskiliTcKimlikNolar) {
                System.out.println("İliskili TcKimlik No: " + iliskiliTcKimlikNo);
                Set<Kisi> ortakKisiler = iliskiliTcKimlikNolarMap.get(iliskiliTcKimlikNo);

                Map<String, List<String>> kisiIliskileri = new HashMap<>();

                for (Kisi kisi : ortakKisiler) {
                    for (Kisi kisiDiger : tumKisiler) {
                        if (kisiDiger.getIliskiliTcKimlikNolar().contains(iliskiliTcKimlikNo)) {
                            String iliskiTipi = kisiDiger.getIliskiTipi(iliskiliTcKimlikNo);
                            kisiIliskileri.computeIfAbsent(kisi.ad + " " + kisi.soyad, k -> new ArrayList<>())
                                    .add(kisiDiger.asilTcKimlikNo + " numaralı kişinin " + iliskiTipi);
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
