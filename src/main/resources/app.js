function doWork(jsonStr, queryStr) {
    let json;
    try {
        json = JSON.parse(jsonStr);
    } catch (e) {
        print('error: Wrong JSON');
        return
    }
    let res;
    try {
        res = jsqry.query(json, queryStr);
    } catch (e) {
        print('error: ' + e);
        return
    }
    print(JSON.stringify(res, null, 2));
}