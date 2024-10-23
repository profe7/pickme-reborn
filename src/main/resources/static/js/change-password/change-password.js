function submitChangePassword() {
  // Ambil nilai dari field input
  const password = document.getElementById("newPassword").value; // Perbaiki ID
  const confirmPassword = document.getElementById("confirmNewPassword").value; // Perbaiki ID
  const token = document.getElementById("token").value;

  // Cek apakah password dan konfirmasi password sama
  if (password !== confirmPassword) {
    document.getElementById("message").innerText =
      "Password dan Konfirmasi Password tidak cocok.";
    return;
  }

  // Validasi panjang password (misalnya minimal 8 karakter)
  if (password.length < 8) {
    document.getElementById("message").innerText =
      "Password harus terdiri dari minimal 8 karakter.";
    return;
  }

  // Siapkan data untuk dikirim
  const data = {
    newPassword: password,
    confirmNewPassword: confirmPassword,
  };

  // Kirim data ke backend menggunakan Fetch API
  fetch(`http://localhost:9002/reset-password/${token}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      // Cek status respons HTTP
      if (response.ok) {
        return response.text(); // atau response.json() jika respon dalam format JSON
      } else {
        throw new Error("Gagal mengubah password.");
      }
    })
    .then((result) => {
      if (result === "Password berhasil diubah.") {
        window.location.href = "/login"; // Redirect ke halaman login setelah berhasil
      } else {
        document.getElementById("message").innerText =
          "Gagal mengubah password. Coba lagi.";
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      document.getElementById("message").innerText =
        "Terjadi kesalahan saat menghubungi server.";
    });
}
