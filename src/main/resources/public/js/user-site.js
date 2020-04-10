$(document).ready(() => {
    //// parent create
    $('#btn-parent-create').on('click', () => {
        // get values from form
        const params = paramsFrom(
            'name',
            'passphrase');

        console.log(`create parent with: ${JSON.stringify(params)}`);

        // call api to create parent
        $.ajax({
            method: 'post',
            url: '/api/parent/create',
            data: params
        }).done((res, status) => {
            console.log(`res: ${JSON.stringify(res)}, stat: ${status}`);
        })

    })
})

const paramsFrom = (...fields) => {
    let result = {};
    fields.map(it => {
        result[it] = $(`#${it}`).val();
    })

    return result;
}