function doWork(jsonStr, queryStr, useFirst) {
    let json;
    try {
        json = JSON.parse(jsonStr);
    } catch (e) {
        console.error('error: Wrong JSON');
        return false;
    }
    let res;
    try {
        res = (useFirst ? jsqry.first : jsqry.query)(json, queryStr);
    } catch (e) {
        console.error('error: ' + e);
        return false;
    }
    console.info(JSON.stringify(res, null, 2));
    return true;
}