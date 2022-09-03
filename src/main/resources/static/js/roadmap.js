let progress = document.querySelector("#progress-bar").innerHTML;
let need  = 100;
need = need - progress;

const data = {
    labels: [
        'Progress','Incomplete'

    ],
    datasets: [{
        label: 'My First Dataset',
        data: [progress, need],
        backgroundColor: [
            '#5CDB95',
            'gray'
        ],
        hoverOffset: 4
    }]
};

const config = {
    type: 'doughnut',
    data: data,
};

const myChart = new Chart(
    document.querySelector('#myChart'), config);
