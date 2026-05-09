// Admin Panel Javascript

function showToast(msg, type='success') {
    const c = document.getElementById('toastContainer');
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    t.innerHTML = `<i class="fa-solid fa-${type==='success'?'circle-check':'circle-xmark'}"></i> ${msg}`;
    c.appendChild(t);
    setTimeout(() => t.remove(), 3500);
}

// Modal handling
let currentSubmitAction = null;

function openAdminModal(title, contentHtml, submitAction) {
    document.getElementById('adminModalTitle').textContent = title;
    document.getElementById('adminModalBody').innerHTML = contentHtml;
    currentSubmitAction = submitAction;
    document.getElementById('adminModal').classList.add('active');
}

function closeAdminModal() {
    document.getElementById('adminModal').classList.remove('active');
    document.getElementById('adminModalForm').reset();
    currentSubmitAction = null;
}

async function submitAdminForm(e) {
    e.preventDefault();
    if (currentSubmitAction) {
        await currentSubmitAction();
    }
}

async function fetchApi(url, options = {}) {
    try {
        const res = await fetch(url, options);
        if (!res.ok) {
            const errText = await res.text();
            throw new Error(errText || 'API Error');
        }
        return await res.json();
    } catch(err) {
        showToast(err.message, 'error');
        throw err;
    }
}

// Stats
async function loadStats() {
    const res = await fetchApi('/api/admin/stats');
    if (res && res.data) {
        const html = `
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#4F46E5;">${res.data.totalStudents}</div><div style="font-size:13px;color:var(--text-secondary);">Students</div>
            </div>
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#06B6D4;">${res.data.totalFaculty}</div><div style="font-size:13px;color:var(--text-secondary);">Faculty</div>
            </div>
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#10b981;">${res.data.totalAttendance}</div><div style="font-size:13px;color:var(--text-secondary);">Attendance Records</div>
            </div>
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#f59e0b;">${res.data.totalResults}</div><div style="font-size:13px;color:var(--text-secondary);">Result Records</div>
            </div>
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#8b5cf6;">${res.data.totalEvents}</div><div style="font-size:13px;color:var(--text-secondary);">Events</div>
            </div>
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#ec4899;">${res.data.totalBooks}</div><div style="font-size:13px;color:var(--text-secondary);">Library Books</div>
            </div>
            <div class="stat-card" style="background:var(--bg-card);padding:20px;border-radius:12px;border:1px solid var(--border);">
                <div style="font-size:24px;font-weight:800;color:#f43f5e;">${res.data.totalFees}</div><div style="font-size:13px;color:var(--text-secondary);">Fee Records</div>
            </div>
        `;
        document.getElementById('dashboardStatsGrid').innerHTML = html;
    }
}

// Students
async function loadStudents() {
    const search = document.getElementById('studentSearch')?.value || '';
    const res = await fetchApi(`/api/admin/students?search=${search}`);
    const tbody = document.getElementById('studentsTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(s => `
            <tr>
                <td><strong>${s.registrationNumber || '-'}</strong></td>
                <td>${s.name}</td>
                <td>${s.email}</td>
                <td><span class="nav-badge" style="background:#4F46E5;">${s.department || '-'}</span></td>
                <td>${s.semester || '-'}</td>
                <td>
                    <button class="action-btn btn-edit" onclick="editStudent(${s.id}, '${s.name}', '${s.email}', '${s.department}', '${s.section}', '${s.semester}', '${s.registrationNumber}')"><i class="fa-solid fa-pen"></i></button>
                    <button class="action-btn btn-delete" onclick="deleteStudent(${s.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

function editStudent(id, name, email, dept, sec, sem, reg) {
    const html = `
        <div class="form-group"><label>Name</label><input type="text" id="stuName" value="${name}"></div>
        <div class="form-group"><label>Email</label><input type="email" id="stuEmail" value="${email}"></div>
        <div class="form-group"><label>Reg No</label><input type="text" id="stuReg" value="${reg!=='null'?reg:''}"></div>
        <div class="form-group"><label>Department</label><input type="text" id="stuDept" value="${dept!=='null'?dept:''}"></div>
        <div class="form-group"><label>Section</label><input type="text" id="stuSec" value="${sec!=='null'?sec:''}"></div>
        <div class="form-group"><label>Semester</label><input type="number" id="stuSem" value="${sem!=='null'?sem:''}"></div>
    `;
    openAdminModal('Edit Student', html, async () => {
        const body = {
            name: document.getElementById('stuName').value,
            email: document.getElementById('stuEmail').value,
            registrationNumber: document.getElementById('stuReg').value,
            department: document.getElementById('stuDept').value,
            section: document.getElementById('stuSec').value,
            semester: parseInt(document.getElementById('stuSem').value) || null
        };
        await fetchApi(`/api/admin/students/${id}`, { method: 'PUT', headers: {'Content-Type':'application/json'}, body: JSON.stringify(body) });
        showToast('Student updated successfully');
        closeAdminModal();
        loadStudents();
    });
}

async function deleteStudent(id) {
    if (confirm('Are you sure you want to delete this student?')) {
        await fetchApi(`/api/admin/students/${id}`, { method: 'DELETE' });
        showToast('Student deleted');
        loadStudents();
    }
}

// Attendance
async function loadAttendance() {
    const res = await fetchApi('/api/admin/attendance/all');
    const tbody = document.getElementById('attendanceTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(a => `
            <tr>
                <td><strong>${a.studentName}</strong></td>
                <td>${a.subjectName} <br><span style="font-size:11px;color:var(--text-secondary);">${a.subjectCode}</span></td>
                <td>${a.date}</td>
                <td><span class="nav-badge" style="background:${a.status==='PRESENT'?'#10b981':(a.status==='ABSENT'?'#ef4444':'#f59e0b')};">${a.status}</span></td>
                <td>${a.markedByName || 'System'}</td>
                <td>
                    <button class="action-btn btn-delete" onclick="deleteAttendance(${a.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

async function deleteAttendance(id) {
    if (confirm('Delete this attendance record?')) {
        await fetchApi(`/api/attendance/${id}`, { method: 'DELETE' });
        showToast('Attendance deleted');
        loadAttendance();
        loadStats();
    }
}

// Results
async function loadResults() {
    const res = await fetchApi('/api/admin/results/all');
    const tbody = document.getElementById('resultsTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(r => `
            <tr>
                <td><strong>${r.studentName}</strong></td>
                <td>${r.subjectName}</td>
                <td>${r.examType}</td>
                <td>${r.marksObtained} / ${r.totalMarks}</td>
                <td><strong>${r.grade || '-'}</strong></td>
                <td>
                    <button class="action-btn btn-delete" onclick="deleteResult(${r.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

async function deleteResult(id) {
    if (confirm('Delete this result record?')) {
        await fetchApi(`/api/results/${id}`, { method: 'DELETE' });
        showToast('Result deleted');
        loadResults();
        loadStats();
    }
}

// Events
async function loadEvents() {
    const res = await fetchApi('/api/admin/events/all');
    const tbody = document.getElementById('eventsTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(e => `
            <tr>
                <td><strong>${e.title}</strong></td>
                <td>${e.eventDate}</td>
                <td>${e.location || '-'}</td>
                <td><span class="nav-badge" style="background:#8b5cf6;">${e.category}</span></td>
                <td>${e.isActive ? '<span style="color:#10b981;">Active</span>' : '<span style="color:#ef4444;">Inactive</span>'}</td>
                <td>
                    <button class="action-btn btn-delete" onclick="deleteEvent(${e.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

async function deleteEvent(id) {
    if (confirm('Delete this event?')) {
        await fetchApi(`/api/events/${id}`, { method: 'DELETE' });
        showToast('Event deleted');
        loadEvents();
        loadStats();
    }
}

// Library
async function loadBooks() {
    const res = await fetchApi('/api/admin/library/all');
    const tbody = document.getElementById('booksTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(b => `
            <tr>
                <td><strong>${b.title}</strong></td>
                <td>${b.author}</td>
                <td>${b.isbn || '-'}</td>
                <td>${b.totalCopies}</td>
                <td><strong style="color:${b.availableCopies>0?'#10b981':'#ef4444'};">${b.availableCopies}</strong></td>
                <td>
                    <button class="action-btn btn-delete" onclick="deleteBook(${b.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

async function deleteBook(id) {
    if (confirm('Delete this book?')) {
        await fetchApi(`/api/library/books/${id}`, { method: 'DELETE' });
        showToast('Book deleted');
        loadBooks();
        loadStats();
    }
}

// Fees
async function loadFees() {
    const res = await fetchApi('/api/admin/fees/all');
    const tbody = document.getElementById('feesTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(f => `
            <tr>
                <td><strong>${f.studentName}</strong></td>
                <td>${f.feeType}</td>
                <td>₹${f.amount}</td>
                <td>${f.dueDate}</td>
                <td><span class="nav-badge" style="background:${f.status==='PAID'?'#10b981':(f.status==='PENDING'?'#f59e0b':'#ef4444')};">${f.status}</span></td>
                <td>
                    <button class="action-btn btn-delete" onclick="deleteFee(${f.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

async function deleteFee(id) {
    if (confirm('Delete this fee record?')) {
        await fetchApi(`/api/fees/${id}`, { method: 'DELETE' });
        showToast('Fee record deleted');
        loadFees();
        loadStats();
    }
}

// Exams
async function loadExams() {
    const res = await fetchApi('/api/admin/examinations/all');
    const tbody = document.getElementById('examsTbody');
    if (res && res.data) {
        tbody.innerHTML = res.data.map(ex => `
            <tr>
                <td><strong>${ex.subjectName}</strong> <br><span style="font-size:11px;color:var(--text-secondary);">${ex.subjectCode}</span></td>
                <td>${ex.examType}</td>
                <td>${ex.examDate}</td>
                <td>${ex.startTime} - ${ex.endTime}</td>
                <td>${ex.room || '-'}</td>
                <td>
                    <button class="action-btn btn-delete" onclick="deleteExam(${ex.id})"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

async function deleteExam(id) {
    if (confirm('Delete this examination schedule?')) {
        await fetchApi(`/api/examinations/${id}`, { method: 'DELETE' });
        showToast('Exam deleted');
        loadExams();
        loadStats();
    }
}

// Init dashboard stats on page load
document.addEventListener('DOMContentLoaded', () => {
    loadStats();
    
    // Add event listener for student search
    document.getElementById('studentSearch')?.addEventListener('keyup', (e) => {
        if(e.key === 'Enter') loadStudents();
    });
});
