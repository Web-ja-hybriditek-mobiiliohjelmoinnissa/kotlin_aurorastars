# AuroraStars 🌌

AuroraStars on natiivi Android-sovellus revontulien, sään ja tähtitaivaan seurantaan. Sovellus on toteutettu modernilla Android-teknologialla (Kotlin + Jetpack Compose) ja se hyödyntää MVVM-arkkitehtuuria.


## Demo videos
https://www.youtube.com/watch?v=VsNdKL00u9c

## 📱 Ominaisuudet

* **Revontuliennuste:** Laskee revontulien todennäköisyyden (%) käyttäjän sijainnin (leveyspiiri) ja reaaliaikaisen Kp-indeksin perusteella.
* **Sääennuste:** Reaaliaikainen sää, tuulen nopeus ja pilvisyysennuste (kriittinen revontulien havainnointiin).
* **Tähtikartta:** Interaktiivinen, sijaintiin perustuva tähtitaivas (VirtualSky-integraatio).
* **Reaaliaikainen kello:** Sekunnilleen synkronoitu kello.
* **Sijaintipalvelut:** Automaattinen GPS-paikannus ja paikan nimen haku (Reverse Geocoding).

## 🛠️ Teknologiat & Arkkitehtuuri

Sovellus on rakennettu noudattaen **Grade 5** -tason vaatimuksia ja Clean Architecture -periaatteita.

* **Kieli:** 100% Kotlin
* **UI:** Jetpack Compose (Material3 Design)
* **Arkkitehtuuri:** MVVM (Model-View-ViewModel)
    * **Repository Pattern:** Erottaa datalähteet sovelluslogiikasta.
    * **StateFlow:** Reaktiivinen tilanhallinta.
* **Verkkoliikenne:** Ktor Client (Asynkroninen datahaku).
* **Concurrency:** Kotlin Coroutines & Flow.

## 📡 Rajapinnat (API)

Sovellus yhdistää dataa useasta eri lähteestä:

1.  **NOAA Space Weather Prediction Center:** Kp-indeksit ja avaruussää.
2.  **Open-Meteo:** Säätiedot ja pilvisyysennusteet.
3.  **VirtualSky (LCO):** Dynaaminen tähtikarttanäkymä.
4.  **Android Geocoder:** Koordinaattien muunto paikannimiksi.

## 🚀 Asennus ja käyttö

Projekti ei vaadi erillisiä API-avaimia toimiakseen, sillä käytetyt rajapinnat ovat avoimia.


## 📜 Lisenssi

Tämä projekti on kurssityö.
Lähdeaineistot: NOAA (Public Domain), Open-Meteo (CC BY 4.0), VirtualSky (BSD 3-Clause).

---
*Tekijä: Antero Opiskelija | Mobiiliohjelmointi 2025*