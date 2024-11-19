function openTalentModal(button) {
    const mitraId = $(button).data("mitra-id");
    const vacancyId = $(button).data("vacancy-id");

     // Lakukan sesuatu dengan mitraId dan vacancyId
    console.log('Mitra ID:', mitraId);
    console.log('Vacancy ID:', vacancyId);

    if (!mitraId || !vacancyId) {
        console.error("mitraId atau vacancyId tidak ditemukan");
        return;
    }

    $.ajax({
        url: `/vacancies/${mitraId}/${vacancyId}/talent`,
        method: "GET",
        success: function (data) {
            let talentRows = "";
            data.talents.forEach(talent => {
                talentRows += `<tr>
                <td>${talent.talentName}</td>
                <td>${talent.talentPosition}</td>
                <td>${talent.talentSkill}</td>
                <td>
                    <button class="btn btn-sm" style="background-color: #006683; color: white;">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6" style="width: 1em; height: 1em; margin-right: 0.25em;">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z" />
                        </svg>
                    CV
                    </button>

                    <button class="btn btn-sm" style="background-color: #106AFC; color: white;">Pilih</button>
                </td>
            </tr>`;
            });
            $('#talentList').html(talentRows);
            $('#talentModal').modal('show');
        },
        error: function (err) {
            $("#talentList").html("<tr><td colspan='4'>Failed to load talent data.</td></tr>");
        }
    });
}

function closeTalentModal() {
    $("#talentModal").modal("hide");
}

function validateForm(event) {
    // Prevent the default form submission
    event.preventDefault();

    // Reset pesan error
    document.getElementById('namaLengkapError').innerText = "";
    document.getElementById('emailError').innerText = "";
    document.getElementById('nomorKTPError').innerText = "";

    // Mendapatkan nilai input
    const namaLengkap = document.getElementById('namaLengkap').value.trim();
    const email = document.getElementById('email').value.trim();
    const nomorKTP = document.getElementById('nomorKTP').value.trim();
    const mitraId = document.querySelector('button[form="talentForm"]').getAttribute('data-mitra-id');
    const vacancyId = document.querySelector('button[form="talentForm"]').getAttribute('data-vacancy-id');

    let isValid = true;

    // Validasi Nama Lengkap
    if (namaLengkap === "") {
        document.getElementById('namaLengkapError').innerText = "Nama Lengkap wajib diisi.";
        isValid = false;
    }

    // Validasi Email
    if (email === "") {
        document.getElementById('emailError').innerText = "Email wajib diisi.";
        isValid = false;
    } else if (!/\S+@\S+\.\S+/.test(email)) {
        document.getElementById('emailError').innerText = "Format email tidak valid.";
        isValid = false;
    }

    // Validasi Nomor KTP
    if (nomorKTP === "") {
        document.getElementById('nomorKTPError').innerText = "Nomor KTP wajib diisi.";
        isValid = false;
    } else if (!/^\d+$/.test(nomorKTP) || nomorKTP.length !== 16) {
        document.getElementById('nomorKTPError').innerText = "Nomor KTP harus berupa angka 16 digit.";
        isValid = false;
    }

    if (isValid) {
        const requestData = {
            talentName: namaLengkap,
            talentEmail: email,
            talentNik: nomorKTP,
            talentMitraId: mitraId,
            vacancyId: vacancyId
        };

        $.ajax({
            url: "/vacancies/applyNewTalent",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData),
            complete: function (xhr) {
                if (xhr.status === 200) {
                    Swal.fire({
                        title: 'Success',
                        text: xhr.responseText,
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        window.location.href = "/vacancies/" + vacancyId;
                    });
                } else {
                    Swal.fire({
                        title: 'Error',
                        text: xhr.responseText,
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            }
        });
    }
}

// Attach the validateForm function to the form submit event
document.getElementById('talentForm').addEventListener('submit', validateForm);