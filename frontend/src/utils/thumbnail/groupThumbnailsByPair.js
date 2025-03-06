const groupThumbnails = (thumbnails) => {
    const groups = [];
    for (let i = 0; i < thumbnails.length; i += 2) {
        const group = [];
        group.push(thumbnails[i]);
        if (thumbnails[i + 1]) {
            group.push(thumbnails[i + 1]);
        }
        groups.push(group);
    }
    return groups;
};

export default groupThumbnails;