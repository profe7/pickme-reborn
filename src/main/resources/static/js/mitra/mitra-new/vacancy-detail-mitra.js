let selectedTalentIds = [];

function openTalentModal(button) {
    const mitraId = $(button).data("mitra-id");
    const vacancyId = $(button).data("vacancy-id");

    // Function to filter table rows based on search criteria
    function filterTalents() {
        const position = $('input[name="talentPosition"]').val().trim().toLowerCase();
        const skill = $('input[name="talentSkill"]').val().trim().toLowerCase();

        $('#talentList tr').each(function() {
            const rowPosition = $(this).find('td:nth-child(2)').text().toLowerCase();
            const rowSkill = $(this).find('td:nth-child(3)').text().toLowerCase();

            if ((position === '' || rowPosition.includes(position)) && (skill === '' || rowSkill.includes(skill))) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    // Initial fetch without filters
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

                    <button class="btn btn-sm pilih-btn" style="background-color: #106AFC; color: white;" data-talent-id="${talent.talentId}" data-talent-name="${talent.talentName}">Pilih</button>
                </td>
            </tr>`;
            });
            $('#talentList').html(talentRows);
            $('#talentModal').modal('show');

            $('#selectedTalents').addClass('scrollable-container');

            $('.pilih-btn').on('click', function() {
                const talentId = $(this).data('talent-id');
                const talentName = $(this).data('talent-name');
                const miniCard = `<div class="mini-card" data-talent-id="${talentId}">${talentName} <button class="remove-btn">X</button></div>`;
                $('#selectedTalents').append(miniCard);

                selectedTalentIds.push(talentId);

                $(this).prop('disabled', true).addClass('disabled-btn');

                $('.remove-btn').on('click', function() {
                    const talentId = $(this).parent().data('talent-id');
                    $(this).parent().remove();

                    selectedTalentIds = selectedTalentIds.filter(id => id !== talentId);
                });
            });
        },
        error: function (err) {
            $("#talentList").html("<tr><td colspan='4'>Failed to load talent data.</td></tr>");
        }
    });

    $('input[name="talentPosition"], input[name="talentSkill"]').on('input', filterTalents);

    document.querySelector('.btn.w-100[style*="background-color: #006683"]').addEventListener('click', function() {
        if (selectedTalentIds.length === 0) {
            Swal.fire({
                title: 'Error',
                text: 'No talent selected',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        const requestData = {
            vacancyId: vacancyId,
            talentIds: selectedTalentIds
        };

        $.ajax({
            url: "/api/v1/applicant/apply-multiple-talents",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData),
            success: function(response, textStatus, xhr) {
                if (xhr.status === 201) {
                    Swal.fire({
                        title: 'Success',
                        text: 'Applicants created successfully',
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        selectedTalentIds = [];
                        window.location.reload();
                    });
                } else {
                    Swal.fire({
                        title: 'Error',
                        text: 'Unexpected response status: ' + xhr.status,
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            },
            error: function(xhr) {
                Swal.fire({
                    title: 'Error',
                    text: xhr.responseText,
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        });
    });
}

function closeTalentModal() {
    $("#talentModal").modal("hide");
}

function validateForm(event) {
    event.preventDefault();

    document.getElementById('namaLengkapError').innerText = "";
    document.getElementById('emailError').innerText = "";
    document.getElementById('nomorKTPError').innerText = "";

    const namaLengkap = document.getElementById('namaLengkap').value.trim();
    const email = document.getElementById('email').value.trim();
    const nomorKTP = document.getElementById('nomorKTP').value.trim();
    const mitraId = document.querySelector('button[form="talentForm"]').getAttribute('data-mitra-id');
    const vacancyId = document.querySelector('button[form="talentForm"]').getAttribute('data-vacancy-id');

    let isValid = true;

    if (namaLengkap === "") {
        document.getElementById('namaLengkapError').innerText = "Nama Lengkap wajib diisi.";
        isValid = false;
    }

    if (email === "") {
        document.getElementById('emailError').innerText = "Email wajib diisi.";
        isValid = false;
    } else if (!/\S+@\S+\.\S+/.test(email)) {
        document.getElementById('emailError').innerText = "Format email tidak valid.";
        isValid = false;
    }

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

document.getElementById('talentForm').addEventListener('submit', validateForm);