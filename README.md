# Contact Tracer
### Trabajo Práctico para 75.39 Aplicaciones Informáticas (2°C 2020)
Android App que efectúa rastreo de contactos cercanos usando Bluetooth LE y además registra estadías en lugares mediante el escaneo de códigos QR para el check-in y check-out. Su alcance está limitado a lo recién mencionado sumado a la subida de estos datos al backend. La interfaz es extremadamente básica.

#### Librerías:
- Android Jetpack (MVVM, LiveData, Databinding, Room, Navigation, etc).
- [CloseToMe] para el rastreo con Bluetooth LE. Uso basado en la [app de ejemplo del repo](https://github.com/mohsenoid/close-to-me/tree/master/sample-app).
- Google CameraX para el uso de la cámara.
- Google ML Kit (Vision API) para el reconocimiento de códigos QR. Uso, sumado a CameraX, basado en [QRScannerCameraX](https://github.com/natiginfo/QRScannerCameraX).
- Retrofit 2 + Moshi converter para el cliente HTTP.

[CloseToMe]: <https://github.com/mohsenoid/close-to-me>
