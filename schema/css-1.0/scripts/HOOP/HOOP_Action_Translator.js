function translate(pageData, entityId) {
    var uiData = JSON.parse(pageData).chathours_p0;
    var ohsData = {};
    ohsData.timeZone = uiData.timezone;
    ohsData.operationalHours = getOperationalHours(uiData.weekdays.supportweekdays);
    var allException = getHolidaysAndExceptions(uiData.exceptions.supportexceptionsSection.supportexceptions);
    ohsData.holidays = allException.holidays;
    ohsData.exceptions = allException.exceptions;
    return JSON.stringify(ohsData);

}

function getHolidaysAndExceptions(supportexceptions) {
    var holidays = [];
    var allDayOpen = [];
    var exceptions = [];
    for (var i = 0; i < supportexceptions.length; i++) {
        var se = supportexceptions[i];
        if (se.openAllDay) {
            allDayOpen.push(se.date);
        } else if (se.slots.length == 0) {
            holidays.push(se.date);
        } else {
            var shifts = processShifts(se.slots);
            var j = getIndexofSimilarShift(exceptions, shifts);
            if (j !== -1) {
                exceptions[j].days.push(se.date);
            } else {
                var opHours = {};
                opHours.days = [se.date];
                opHours.shifts = shifts;
                exceptions.push(opHours);
            }
        }
    }
    if (allDayOpen.length !== 0) {
        var opHours = {};
        opHours.days = allDayOpen;
        opHours.shifts = [{
                "start": "0001",
                "end": "2359"
            }
        ];
        exceptions.push(opHours);
    }
    return {
        "holidays": holidays,
        "exceptions": exceptions
    };

}

function getOperationalHours(supportweekdays) {
    var operationalHours = [];
    var days = [
        "sunday",
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday"
    ];
    var data = [];
    var allDayShift = [];
    var weekoffs = [];
    for (index = 0; index < supportweekdays.length; index++) {
        var day = supportweekdays[index];
        var shifts = [];
        var weekDay = days[index];
        if (day.openAllDay) {
            allDayShift.push(weekDay);
        } else if (day.slots.length == 0) {
            weekoffs.push(weekDay);
            continue;
        } else {
            var shifts = processShifts(day.slots);
            var j = getIndexofSimilarShift(operationalHours, shifts);
            if (j !== -1) {
                operationalHours[j].days.push(weekDay);
            } else {
                var opHours = {};
                opHours.days = [weekDay];
                opHours.shifts = shifts;
                operationalHours.push(opHours);
            }
        }
        data.push(day.openAllDay);

    }
    if (allDayShift.length !== 0) {
        var opHours = {};
        opHours.days = allDayShift;
        opHours.shifts = [{
                "start": "0001",
                "end": "2359"
            }
        ];
        operationalHours.push(opHours);
    }
    return operationalHours;
}

function getIndexofSimilarShift(operationalHours, shifts) {
    var foundIndex = -1;
    for (var index = 0; index < operationalHours.length; index++) {
        var opHours = operationalHours[index];
        if (shifts.length !== opHours.shifts.length)
            continue;
        var sameShiftCount = 0;
        for (var i = 0; i < shifts.length; i++) {
            var shift = shifts[i];
            var fS = find(opHours.shifts, function (s) {
                return s.start == shift.start
            });
            if (!fS || fS.end !== shift.end) {
                continue;
            } else ++sameShiftCount;
        }
        if (sameShiftCount === shifts.length) {
            foundIndex = index;
        }
    }
    return foundIndex;
}

function find(arr, cb) {
    var found = arr.filter(cb);
    if (found.length == 0)
        return undefined;
    else
        return found[0];

}
function processShifts(slots) {
    for (var i = 0; i < slots.length; i++) {
        var slot = slots[i];
        slot.start = slot.start.split(':').map(function (item) {
            return item.trim()
        }).join("");
        slot.end = slot.end.split(':').map(function (item) {
            return item.trim();
        }).join("");
        if (slot.end === "0000") {
            slot.end = "2359"
        }
    }
    return slots;
}