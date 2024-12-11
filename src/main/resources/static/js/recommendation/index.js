// Function to filter talents by name
function filterTalentsName() {
    let talentName = document.getElementById('searchTalentName').value;
    let position = document.getElementById('searchTalentPosition').value;
    let clientId = document.getElementById('clientId').value;

    fetchRecommendations(clientId, talentName, position);
}

// Function to filter talents by position
function filterTalentsPosition() {
    let talentName = document.getElementById('searchTalentName').value;
    let position = document.getElementById('searchTalentPosition').value;
    let clientId = document.getElementById('clientId').value;

    fetchRecommendations(clientId, talentName, position);
}

// Function to fetch recommendations from the server
function fetchRecommendations(clientId, talentName, position) {
    $.ajax({
        url: `/client/recommendations`,
        type: 'GET',
        data: {
            clientId: clientId,
            talentName: talentName,
            position: position
        },
        success: function(data) {
            $('#talent-recommendations').empty().html($(data).find('#talent-recommendations').html());
            $('#pagination-controls').empty().html($(data).find('#pagination-controls').html());
        },
        error: function(error) {
            console.error('Error fetching recommendations:', error);
        }
    });
}

document.getElementById('interviewForm').addEventListener('submit', function(event) {
    event.preventDefault();

    let position = document.querySelector('[data-bs-target="#scheduleInterviewModal"]').getAttribute('data-position');
    let locationAddress = document.getElementById('alamat').value;
    let interviewLink = document.getElementById('linkInterview').value;
    let interviewTypeValue = document.getElementById('tipeWawancara').value;
    let interviewType = interviewTypeValue === '0' ? 'ONLINE' : 'OFFLINE';
    let date = document.getElementById('tanggal').value;
    let startTime = document.getElementById('waktuMulai').value;
    let endTime = document.getElementById('waktuSelesai').value;
    let talentId = document.getElementById('talentId').value;

    if (!position || !date || !startTime || !endTime || !talentId ||
        (interviewType === 'ONLINE' && !interviewLink) ||
        (interviewType === 'OFFLINE' && !locationAddress)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please fill in all required fields.'
        });
        return;
    }

    let message = "";
    let status = "ON_PROCESS";
    let feedback = "";
    let clientId = document.getElementById('clientId').value;
    let applicantId = "";
    let onBoardDate = "";

    let data = {
        position: position,
        locationAddress: locationAddress,
        interviewLink: interviewLink,
        interviewType: interviewType,
        message: message,
        status: status,
        feedback: feedback,
        clientId: clientId,
        applicantId: applicantId,
        date: date,
        onBoardDate: onBoardDate,
        startTime: startTime,
        endTime: endTime,
        talentId: talentId
    };

    fetch('/api/v1/interview/invite', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'SUCCESS') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: data.message
                }).then(() => {
                    location.reload();
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message
                });
            }
        })
        .catch((error) => {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'An error occurred while inviting the applicant.'
            });
            console.error('Error:', error);
        });
});

document.addEventListener('DOMContentLoaded', function() {
    var tipeWawancara = document.getElementById('tipeWawancara');
    var alamatContainer = document.getElementById('alamatContainer');
    var linkInterviewContainer = document.getElementById('linkInterviewContainer');

    // Set initial state based on the selected value
    function updateVisibility() {
        if (tipeWawancara.value === '0') { // Online
            alamatContainer.style.display = 'none';
            linkInterviewContainer.style.display = 'block';
        } else if (tipeWawancara.value === '1') { // Offline
            alamatContainer.style.display = 'block';
            linkInterviewContainer.style.display = 'none';
        } else {
            alamatContainer.style.display = 'none';
            linkInterviewContainer.style.display = 'none';
        }
    }

    // Update visibility on change
    tipeWawancara.addEventListener('change', updateVisibility);

    // Initial call to set the correct state
    updateVisibility();
});