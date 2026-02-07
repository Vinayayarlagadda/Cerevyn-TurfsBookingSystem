const API_BASE_URL = "http://localhost:8090";


document.addEventListener("DOMContentLoaded", () => {
  handleAuthUI();
  fetchTurfs();
});


function handleAuthUI() {
  const user = localStorage.getItem("loggedInUser");

  const loginBtn = document.getElementById("loginBtn");
  const registerBtn = document.getElementById("registerBtn");
  const logoutBtn = document.getElementById("logoutBtn");
  const addTurfBtn = document.getElementById("addTurfBtn");

  if (user) {
    loginBtn && (loginBtn.style.display = "none");
    registerBtn && (registerBtn.style.display = "none");
    logoutBtn && (logoutBtn.style.display = "inline-block");
  } else {
    addTurfBtn && (addTurfBtn.style.display = "none");
  }
}


function logoutUser() {
  localStorage.removeItem("loggedInUser");
  alert("Logged out successfully");
  window.location.href = "login.html";
}


function registerUser() {
  const name = document.getElementById("registerName").value;
  const email = document.getElementById("registerEmail").value;
  const password = document.getElementById("registerPassword").value;
  const message = document.getElementById("registerMessage");

  fetch(`${API_BASE_URL}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password })
  })
  .then(res => {
    if (!res.ok) throw new Error("Registration failed");
    return res.json();
  })
  .then(() => {
    alert("✅ Registered successfully! Please login.");
    window.location.href = "login.html";
  })
  .catch(err => {
    message.innerText = err.message;
  });
}


function loginUser() {
  const email = document.getElementById("loginEmail").value;
  const password = document.getElementById("loginPassword").value;
  const message = document.getElementById("loginMessage");

  fetch(`${API_BASE_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  })
  .then(res => {
    if (!res.ok) throw new Error("Invalid email or password");
    return res.json();
  })
  .then(data => {
    localStorage.setItem("loggedInUser", JSON.stringify({
      id: data.id,
      name: data.name,
      email: data.email
    }));

    alert("✅ Login successful! See you soon..");
    window.location.href = "index.html";
  })
  .catch(err => {
    message.innerText = err.message;
  });
}


function fetchTurfs() {
  const container = document.getElementById("turfCardsContainer");
  if (!container) return;

  fetch(`${API_BASE_URL}/api/turfs`)
    .then(res => res.json())
    .then(data => renderTurfs(data))
    .catch(err => console.error(err));
}

function renderTurfs(turfs) {
  const container = document.getElementById("turfCardsContainer");
  container.innerHTML = "";

  turfs.forEach(turf => {
    const card = document.createElement("div");
    card.className = "turf-card";

    card.innerHTML = `
      <img src="${API_BASE_URL}/images/${turf.imageUrl}">
      <h3>${turf.name}</h3>
      <p>📍 ${turf.location}</p>
      <p>💰 ₹${turf.pricePerHour} / hour</p>
      <button onclick="handleBookTurf(${turf.id})">Book Turf</button>
    `;

    container.appendChild(card);
  });
}


function handleBookTurf(turfId) {
  const userStr = localStorage.getItem("loggedInUser");

  if (!userStr) {
    alert("Please login to book a turf");
    window.location.href = "login.html";
    return;
  }

  const user = JSON.parse(userStr);

  const bookingData = {
    userId: user.id,
    turfId: turfId,
    date: new Date().toISOString().split("T")[0],
    startTime: "10:00",
    endTime: "11:00"
  };

  fetch(`${API_BASE_URL}/api/bookings`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(bookingData)
  })
  .then(res => {
    if (!res.ok) throw new Error("Booking failed");
    return res.json();
  })
  .then(() => {
    alert("🎉 Booked successfully!");
  })
  .catch(err => alert(err.message));
}
function addTurf() {
  const name = document.getElementById("turfName").value.trim();
  const location = document.getElementById("turfLocation").value.trim();
  const price = document.getElementById("turfPrice").value;
  const imageUrl = document.getElementById("turfImage").value.trim();
  const message = document.getElementById("addTurfMessage");

  if (!name || !location || !price || !imageUrl) {
    message.style.color = "red";
    message.innerText = "All fields are required";
    return;
  }

  fetch(`${API_BASE_URL}/api/turfs`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      name: name,
      location: location,
      pricePerHour: price,
      imageUrl: imageUrl
    })
  })
  .then(res => {
    if (!res.ok) {
      return res.text().then(msg => { throw new Error(msg); });
    }
    return res.json();
  })
  .then(() => {
    alert("✅ Turf added successfully!");
    window.location.href = "index.html"; // ✅ REDIRECT
  })
  .catch(err => {
    message.style.color = "red";
    message.innerText = "❌ Failed to add turf";
    console.error(err);
  });
}
