document.querySelectorAll(".badge").forEach((element) => {
  const status = element.textContent.trim();

  let backgroundColor;
  switch (status) {
    case "ACCEPTED":
      backgroundColor = "#008000"; // Hijau
      break;
    case "REJECTED":
      backgroundColor = "#FF0000"; // Merah
      break;
    case "ON_PROCESS":
      backgroundColor = "#0000FF"; // Biru
      break;
    case "RESCHEDULED":
      backgroundColor = "#808080"; // Abu-abu
      break;
    case "CANCELLED":
      backgroundColor = "#FFFF00"; // Kuning
      break;
    case "INACTIVE":
      backgroundColor = "#808080"; // Abu-abu
      break;
    default:
      backgroundColor = "gray"; // Default jika tidak ada status yang dikenali
      break;
  }

  element.style.backgroundColor = backgroundColor;
});

document.querySelectorAll(".createdAtElement").forEach((element) => {
  const createdAt = new Date(element.textContent);
  if (isNaN(createdAt)) return;

  const year = createdAt.getFullYear();
  const month = (createdAt.getMonth() + 1).toString().padStart(2, "0");
  const day = createdAt.getDate().toString().padStart(2, "0");

  const formattedDate = `${year}-${month}-${day}`;

  element.textContent = formattedDate;
});
