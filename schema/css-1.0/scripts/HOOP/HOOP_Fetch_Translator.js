function translate(values, entity) {
    var weekdays = {
        "sunday": 0,
        "monday": 1,
        "tuesday": 2,
        "wednesday": 3,
        "thursday": 4,
        "friday": 5,
        "saturday": 6
    };
    var uiData = {
        "timezone": [],
        "weekdays": {
            "supportweekdays": [
                {
                    "openAllDay": false,
                    "slots": []
                },
                {
                    "openAllDay": false,
                    "slots": []
                },
                {
                    "openAllDay": false,
                    "slots": []
                },
                {
                    "openAllDay": false,
                    "slots": []
                },
                {
                    "openAllDay": false,
                    "slots": []
                },
                {
                    "openAllDay": false,
                    "slots": []
                },
                {
                    "openAllDay": false,
                    "slots": []
                }
            ],
        },
        "exceptions": {
            "supportexceptionsSection": {
                "supportexceptions": []
            }
        }
    };
    try {
        var apiData = JSON.parse(values);
        uiData.timezone = apiData.timeZone || "Asia/Calcutta";
        for (var i = 0; i < apiData.operationalHours.length; i++) {
            var opHr = apiData.operationalHours[i];
            var openAllDay = false;
            if (opHr.shifts[0].start == "0001" && opHr.shifts[0].end == "2359") {
                openAllDay = true;
            } else if (opHr.shifts[0].end == "2359") {
                opHr.shifts[0].end = "0000";
            }
            var slots = [];
            if (!openAllDay) {
                for (var k = 0; k < opHr.shifts.length; k++) {
                    var shift = opHr.shifts[k];
                    shift.start = shift.start.substring(0, 2) + ":" + shift.start.substring(2, 4);
                    shift.end = shift.end.substring(0, 2) + ":" + shift.end.substring(2, 4);
                    slots.push(shift);
                }
            }
            for (var j = 0; j < opHr.days.length; j++) {
                var day = opHr.days[j];
                var dayData = {
                    "openAllDay": openAllDay,
                    "slots": slots
                };
                uiData.weekdays.supportweekdays[weekdays[day]] = dayData;
            }
        }
        if (apiData.exceptions) {
            for (var i = 0; i < apiData.exceptions.length; i++) {
                var opHr = apiData.exceptions[i];
                var openAllDay = false;
                if (opHr.shifts[0].start == "0001" && opHr.shifts[0].end == "2359") {
                    openAllDay = true;
                } else if (opHr.shifts[0].end == "2359") {
                    opHr.shifts[0].end = "0000";
                }
                var slots = [];
                if (!openAllDay) {
                    for (var k = 0; k < opHr.shifts.length; k++) {
                        var shift = opHr.shifts[k];
                        shift.start = shift.start.substring(0, 2) + ":" + shift.start.substring(2, 4);
                        shift.end = shift.end.substring(0, 2) + ":" + shift.end.substring(2, 4);
                        slots.push(shift);
                    }
                } for (var j = 0; j < opHr.days.length; j++) {
                    var dayData = {
                        "date": opHr.days[j],
                        "openAllDay": openAllDay,
                        "slots": slots
                    };
                    uiData.exceptions.supportexceptionsSection.supportexceptions.push(dayData);
                }
            }
		}
		if (apiData.holidays) {
            var holidays = apiData.holidays;
            for (var i = 0; i < holidays.length; i++) {
                var dayData = {
                    "date": holidays[i],
                    "openAllDay": false,
                    "slots": []
                };
                uiData.exceptions.supportexceptionsSection.supportexceptions.push(dayData);
            }
        }
	} 
	catch (e) { }
    return JSON.stringify(uiData);
}