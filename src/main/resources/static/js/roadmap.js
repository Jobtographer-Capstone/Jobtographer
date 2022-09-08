let allProgress = document.querySelectorAll(".progress-bar");
let i = 0;
for(let progress of allProgress){
    progress = progress.innerHTML;
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
                '#05386B',
                'lightgray'
            ],
            hoverOffset: 4
        }]
    };

    const config = {
        type: 'doughnut',
        data: data,
    };
let charts = document.querySelectorAll('.myChart');
    const myChart = new Chart(
        charts[i], config);
    i++;

}

