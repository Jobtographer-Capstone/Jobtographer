let progress = document.querySelector("#progress-bar").innerHTML;
let need  = 100 - progress;

const data = {
    labels: [
        'Progress','Not complete'

    ],
    datasets: [{
        label: 'My First Dataset',
        data: [progress, need],
        backgroundColor: [
            'rgb(255, 99, 132)',
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
