import dateFormat from "dateformat";
import moment from 'moment';

const Helper = {
    timeToReadCalculator: (post) => {
        const titleLength = post.title.length;
        const descriptionLength = post.description.length;
        const contentLength = post.content.length;
        const totalLength = titleLength + descriptionLength + contentLength;

        const timeToRead = totalLength / 200;

        if (timeToRead < 1) {
            const intPart = Math.trunc(timeToRead); // returns 3
            const floatPart = Number((timeToRead - intPart).toFixed(2));
            return Number(floatPart * 0.6).toFixed(2);
        }
        return timeToRead;
    }
    ,
    formatCreatedDate: (date) => {
        let dateObject = new Date(date);
        let datePart = dateFormat(dateObject, "mmmm d, yyyy ");
        let timePart = dateFormat(dateObject, "HH:MM:ss");
        return `${datePart} at ${timePart}`;
    },
    formatCreatedDate1: (date) => {
        let dateObject = new Date(date);
        let datePart = dateFormat(dateObject, "mmmm d, yyyy ");
        let timePart = dateFormat(dateObject, "HH:MM");
        return `${timePart}, ${datePart}`;
    }
    ,
    getTodayDateTime: (date) => {
        let today = new Date(date);
        let datePart = dateFormat(today, 'yyyy-mm-dd');
        let timePart = dateFormat(today, 'HH:MM:ss');
        return `${datePart}T${timePart}`;
    }
    ,
    getDefaultDateTime: () => {
        let today = new Date();
        let datePart = dateFormat(today, 'yyyy-mm-dd');
        let timePart = dateFormat(today, 'HH:MM:ss');

        // console.log('Date part: ', datePart);
        // console.log('Time part: ', timePart);

        return `${datePart}T${timePart}`;
    },
    getSentDate: (date) => {
        let xyz = new Date(date);
        // xyz.setHours(xyz.getHours() - 7);
        let datePart = dateFormat(xyz, "mmmm d, yyyy");
        let timePart = dateFormat(xyz, "hh:MM TT");

        // return moment(date).format('HH:mm:ss, MMMM Do YYYY');

        return `${datePart} at ${timePart}`;
    },
    formatCreatedDate2: (date) => {
        let dateObject = new Date(date);
        let datePart = dateFormat(dateObject, "mmmm d, yyyy ");
        return `${datePart}`;
    }
}


export default Helper;

