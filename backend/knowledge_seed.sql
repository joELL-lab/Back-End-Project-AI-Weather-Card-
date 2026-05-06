-- =====================================================
-- WeatherWise.ai - Knowledge Base Seed Data
-- Untuk fitur RAG (Retrieval-Augmented Generation)
-- =====================================================

USE weatherwise_db;

CREATE TABLE IF NOT EXISTS knowledge_documents (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    category    VARCHAR(100)    NOT NULL,
    title       VARCHAR(200)    NOT NULL,
    content     TEXT            NOT NULL,
    keywords    VARCHAR(500)    DEFAULT NULL,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Kategori: Cuaca & Kesehatan
-- =====================================================
INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kesehatan', 'Tips Saat Cuaca Panas', 
'Saat cuaca panas (suhu di atas 33°C), penting untuk minum air putih minimal 2-3 liter per hari. Hindari aktivitas berat di luar ruangan antara pukul 10.00-15.00. Gunakan sunscreen SPF 30+ dan pakai topi lebar. Kenakan pakaian berbahan katun yang longgar dan berwarna terang. Waspadai tanda-tanda heat stroke seperti pusing, mual, dan kulit panas kering.',
'panas,hot,terik,matahari,dehidrasi,sunburn,heatstroke,suhu tinggi');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kesehatan', 'Tips Saat Hujan dan Banjir',
'Saat musim hujan, selalu siapkan payung dan jas hujan. Jika terjadi banjir, hindari berjalan di air banjir karena risiko leptospirosis dan elektrokusi. Setelah kehujanan, segera ganti baju kering dan minum minuman hangat. Konsumsi vitamin C untuk menjaga daya tahan tubuh. Jaga kebersihan rumah agar tidak menjadi sarang nyamuk demam berdarah.',
'hujan,rain,banjir,flood,basah,nyamuk,demam,leptospirosis,payung');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kesehatan', 'Polusi Udara dan Kesehatan',
'Air Quality Index (AQI) di atas 100 dianggap tidak sehat. Pada AQI tinggi, gunakan masker N95 saat keluar rumah. Kurangi aktivitas outdoor, terutama olahraga berat. Tutup jendela rumah dan gunakan air purifier jika ada. Kelompok rentan (anak-anak, lansia, penderita asma) harus ekstra hati-hati. Makan makanan kaya antioksidan seperti buah dan sayur hijau.',
'polusi,pollution,aqi,udara,asap,masker,kabut,haze,smog');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kesehatan', 'Tips Cuaca Dingin',
'Pada suhu di bawah 15°C, kenakan pakaian berlapis (layering). Lapisan dalam sebaiknya berbahan moisture-wicking, lapisan tengah insulator (fleece/wool), dan lapisan luar tahan angin/air. Jaga suhu tubuh dengan minum minuman hangat. Hindari paparan angin dingin yang bisa menyebabkan hypothermia. Oleskan pelembab untuk mencegah kulit kering.',
'dingin,cold,sejuk,beku,frost,salju,snow,winter,musim dingin,hypothermia');

-- =====================================================
-- Kategori: Aktivitas & Rekreasi
-- =====================================================
INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('aktivitas', 'Aktivitas Indoor Saat Hujan',
'Saat hujan deras, banyak aktivitas menyenangkan yang bisa dilakukan di dalam ruangan: memasak comfort food seperti sup atau bakso, menonton film atau series marathon, membaca buku dengan secangkir teh hangat, bermain board games bersama keluarga, melakukan yoga atau meditasi, belajar keterampilan baru secara online, atau mengerjakan puzzle.',
'hujan,indoor,dalam ruangan,bosan,rain,aktivitas,kegiatan,rumah');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('aktivitas', 'Olahraga Outdoor Saat Cerah',
'Cuaca cerah dengan suhu 25-30°C ideal untuk olahraga outdoor. Pilihan yang bagus: jogging pagi atau sore hari, bersepeda di taman kota, berenang, hiking di pegunungan terdekat, bermain badminton atau sepak bola, atau sekadar jalan santai di taman. Selalu bawa air minum dan lakukan pemanasan sebelum olahraga.',
'cerah,clear,sunny,olahraga,sport,outdoor,jogging,sepeda,renang,hiking');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('aktivitas', 'Wisata Cuaca Berawan',
'Cuaca berawan (cloudy) sebenarnya ideal untuk wisata karena tidak terlalu panas. Cocok untuk: mengunjungi tempat wisata alam, fotografi outdoor (cahaya difus bagus untuk foto), jalan-jalan di kota tua, mengunjungi kebun binatang atau taman hiburan, piknik di taman, atau tour kuliner keliling kota.',
'berawan,cloudy,mendung,wisata,jalan-jalan,piknik,foto,tourism');

-- =====================================================
-- Kategori: Fenomena Cuaca
-- =====================================================
INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('fenomena', 'Memahami Indeks UV',
'Indeks UV mengukur intensitas radiasi ultraviolet matahari. Skala: 0-2 (rendah), 3-5 (sedang), 6-7 (tinggi), 8-10 (sangat tinggi), 11+ (ekstrem). Pada UV index 3+, gunakan sunscreen. Pada UV 6+, kurangi paparan matahari langsung. UV tertinggi biasanya pukul 10.00-14.00. Awan tebal bisa mengurangi UV hingga 80%, tapi awan tipis hanya 20%.',
'uv,ultraviolet,matahari,radiasi,sunscreen,sunburn,kulit');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('fenomena', 'Memahami Tekanan Udara',
'Tekanan udara normal di permukaan laut sekitar 1013 hPa. Tekanan turun = cuaca memburuk (hujan, badai). Tekanan naik = cuaca membaik (cerah). Perubahan tekanan mendadak bisa menyebabkan sakit kepala pada orang sensitif. Tekanan juga berpengaruh pada perasaan dan energi - tekanan rendah bisa membuat lesu.',
'tekanan,pressure,barometer,hpa,badai,storm,sakit kepala,headache');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('fenomena', 'Kelembapan dan Kenyamanan',
'Kelembapan ideal untuk kenyamanan manusia: 40-60%. Kelembapan di atas 70% terasa gerah dan lengket. Kelembapan di bawah 30% menyebabkan kulit kering, bibir pecah, dan iritasi pernapasan. Kelembapan tinggi membuat suhu terasa lebih panas (heat index). Gunakan dehumidifier pada kelembapan tinggi dan humidifier pada kelembapan rendah.',
'kelembapan,humidity,lembap,gerah,kering,humid,dewpoint');

-- =====================================================
-- Kategori: Kota-kota Indonesia
-- =====================================================
INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kota', 'Cuaca Jakarta',
'Jakarta beriklim tropis dengan suhu rata-rata 27-34°C sepanjang tahun. Musim hujan: November-Maret, musim kemarau: April-Oktober. Jakarta sering mengalami banjir saat musim hujan terutama di area Kampung Melayu, Manggarai, dan Jakarta Utara. AQI Jakarta sering di atas 100 (tidak sehat). Tips: bawa payung dan masker setiap hari.',
'jakarta,dki,ibukota,banjir,macet,polusi,tropis');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kota', 'Cuaca Bali',
'Bali beriklim tropis maritim. Musim kemarau (April-Oktober) adalah waktu terbaik untuk wisata pantai. Musim hujan (November-Maret) cocok untuk wisata budaya dan spa. Suhu rata-rata 27-30°C di pesisir, lebih sejuk di Kintamani dan Bedugul (20-25°C). Surfing terbaik di musim kemarau di Uluwatu dan Kuta.',
'bali,pantai,beach,surfing,kintamani,ubud,denpasar,tropis');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kota', 'Cuaca Bandung',
'Bandung dikenal sebagai "Paris van Java" dengan suhu sejuk 18-28°C. Terletak di dataran tinggi (768m dpl). Hujan bisa turun kapan saja, terutama sore hari. Suhu bisa turun hingga 15°C di malam hari. Kawah Putih dan Tangkuban Perahu lebih dingin lagi (10-15°C). Tips: selalu bawa jaket tipis.',
'bandung,jawa barat,sejuk,dingin,dataran tinggi,kawah putih,tangkuban perahu');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kota', 'Cuaca Surabaya',
'Surabaya beriklim tropis panas dengan suhu rata-rata 28-35°C. Lebih panas dari Jakarta. Musim kemarau (Mei-November) sangat panas dan kering. Angin laut membuat sore hari sedikit lebih sejuk di area pesisir. Kelembapan tinggi sepanjang tahun (70-80%). Tips: minum banyak air dan hindari sinar matahari langsung siang hari.',
'surabaya,jawa timur,panas,hot,kering,lembap,pesisir');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('kota', 'Cuaca Manado',
'Manado beriklim tropis basah dengan curah hujan tinggi sepanjang tahun. Suhu rata-rata 25-31°C. Cocok untuk wisata bawah laut di Taman Nasional Bunaken. Musim terbaik untuk diving: Mei-November (visibility terbaik). Hujan biasanya singkat dan deras. Kuliner khas: tinutuan, cakalang fufu, dan ayam woku.',
'manado,sulawesi,bunaken,diving,snorkeling,laut,hujan,tropis');

-- =====================================================
-- Kategori: Tips Umum
-- =====================================================
INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('tips', 'Cara Membaca Prakiraan Cuaca',
'Beberapa istilah penting: "Cerah berawan" = dominan cerah dengan awan sedikit. "Berawan" = langit tertutup awan 50-80%. "Hujan ringan" = curah hujan < 5mm/jam. "Hujan sedang" = 5-10mm/jam. "Hujan lebat" = > 10mm/jam. "Thunderstorm" = hujan disertai petir. Perhatikan juga arah angin dan kelembapan untuk prakiraan lebih akurat.',
'prakiraan,forecast,prediksi,istilah,cerah,berawan,hujan,thunderstorm');

INSERT INTO knowledge_documents (category, title, content, keywords) VALUES
('tips', 'Pakaian Sesuai Cuaca',
'Panduan pakaian: Panas (>30°C) = kaos tipis, celana pendek, sandal, topi. Hangat (25-30°C) = kaos, celana panjang ringan, sepatu sneakers. Sejuk (20-25°C) = kemeja lengan panjang, celana panjang, jaket tipis. Dingin (<20°C) = sweater/hoodie, celana jeans, jaket tebal, syal. Hujan = bawa jas hujan/payung, hindari sandal licin.',
'pakaian,baju,outfit,clothing,fashion,jaket,kaos,celana');

-- Verifikasi
SELECT CONCAT('Knowledge base loaded: ', COUNT(*), ' documents') AS status FROM knowledge_documents;
