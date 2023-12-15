import { batches } from "/src/services/service.js"

const batchList = [];

async function populateData() {
    let freshBatches = await batches();
    batchList.push(...freshBatches);
    createTable();
}

function createTable() {
    let table = document.getElementById("batches");
    let tableBody = table.getElementsByTagName("tbody")[0];
    tableBody.innerHTML = "";
    batchList.forEach(batch => {
        console.log(batch)
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

populateData();