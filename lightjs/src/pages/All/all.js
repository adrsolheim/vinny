import { batches } from "/src/services/service.js"

const batchColumns = ["id", "brewfatherId", "name", "status", "tapStatus", "packaging"];
const sorts = {
    "id":           (a, b) => a.id - b.id,
    "brewfatherId": (a, b) => a.brewfatherId.toUpperCase() === b.brewfatherId.toUpperCase() ? 0 : a.brewfatherId.toUpperCase() > b.brewfatherId.toUpperCase() ? 1 : -1,
    "name":         (a, b) => a.name.toUpperCase()         === b.name.toUpperCase()         ? 0 :      a.name.toUpperCase()    > b.name.toUpperCase()         ? 1 : -1,
    "status":       (a, b) => a.status.toUpperCase()       === b.status.toUpperCase()       ? 0 :    a.status.toUpperCase()    > b.status.toUpperCase()       ? 1 : -1,
    "tapStatus":    (a, b) => a.tapStatus.toUpperCase()    === b.tapStatus.toUpperCase()    ? 0 : a.tapStatus.toUpperCase()    > b.tapStatus.toUpperCase()    ? 1 : -1,
    "packaging":    (a, b) => a.packaging.toUpperCase()    === b.packaging.toUpperCase()    ? 0 : a.packaging.toUpperCase()    > b.packaging.toUpperCase()    ? 1 : -1,


}
const batchList = [];

function camelCaseToWord(camel) {
    let wlist = camel.match(/[A-Z]?[a-z]*/g);
    return wlist.map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');
}

function createTableHeaders() {
    let table = document.getElementById("batches");
    let tHead = table.getElementsByTagName("thead")[0];
    let tableHeader = document.createElement("tr");
    tHead.appendChild(tableHeader);
    tableHeader.innerHTML = "";
    let thRow = {};
    batchColumns.forEach(col => {
        thRow[col] = document.createElement("th");
        thRow[col].setAttribute("id", `th${col}`);
        thRow[col].textContent = camelCaseToWord(col);
        thRow[col].addEventListener("click", sortBy);
        tableHeader.appendChild(thRow[col]);
    });
}

function sortBy(event) {
    if (event.target.tagName === 'TH') {
        batchList.sort(sorts[event.target.id.slice(2)]);
        renderTable();
    }
}
async function populateData() {
    let freshBatches = await batches();
    batchList.push(...freshBatches);
    renderTable();
}

function renderTable() {
    let table = document.getElementById("batches");
    let tableBody = table.getElementsByTagName("tbody")[0];
    tableBody.innerHTML = "";
    batchList.forEach(batch => {
        let tr = `
        <tr>
            <td>${batch.id}</td>
            <td>${batch.brewfatherId}</td>
            <td>${batch.name}</td>
            <td>${batch.status}</td>
            <td>${batch.tapStatus}</td>
            <td>${batch.packaging}</td>
        </tr>
        `;
        tableBody.innerHTML += tr;
    });
}

createTableHeaders();
populateData();