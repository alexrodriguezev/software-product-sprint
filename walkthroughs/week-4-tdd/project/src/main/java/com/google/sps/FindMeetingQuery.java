// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.lang.Math;
import java.util.stream.Collectors;


public final class FindMeetingQuery {

 /* Given the meeting information, this method
    returns the times when the meeting could happen that day.
 */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> options = new ArrayList<TimeRange>();
    if (Integer.compare((int) request.getDuration(), TimeRange.WHOLE_DAY.duration()) > 0) {
        // If duration of event is more than 24hrs
        return options;
    }

    if (events.equals(Collections.emptySet())) {
        // If there are no events scheduled
        options.add(TimeRange.WHOLE_DAY);
        return options;
    }
    
    // Most recent start time for the chunk of meeting time option
    int currentStart = 0;

    List<Event> eventsList = new ArrayList<Event>(events);
    eventsList = eventsList.stream().sorted(Comparator.comparingInt(Event::getStart)).collect(Collectors.toList());

    // Iterate through event list in order
    for (int i = 0; i < eventsList.size(); i++) {
        if (eventsList.get(i).getWhen().start() < (TimeRange.START_OF_DAY - 1)|| eventsList.get(i).getWhen().end() > (TimeRange.END_OF_DAY + 1)) {
            // If time range is out of bounds, continue to next event
            continue;
        }

        if (Collections.disjoint(eventsList.get(i).getAttendees(), request.getAttendees())) {
            // If event attendees and request attendees have no names in common
            continue;
        }

        if (eventsList.get(i).getWhen().start() <= currentStart) {
            // If current event overlaps with an event that's already going on and it's start time < currentStart
            currentStart = Math.max(currentStart, eventsList.get(i).getWhen().end());
            continue;
        }

        System.out.println(currentStart);
        if ((eventsList.get(i).getWhen().start() - request.getDuration()) >= currentStart) {
            // If there's time before event i starts, add it as a chunk of meeting time options
            options.add(TimeRange.fromStartEnd(currentStart, eventsList.get(i).getWhen().start(), false));
        }

        // Update currentStart
        currentStart = eventsList.get(i).getWhen().end();    
    }

    if (TimeRange.WHOLE_DAY.end() - request.getDuration() > currentStart) {
        // If there's time left at the end of the day
        options.add(TimeRange.fromStartEnd(currentStart, TimeRange.WHOLE_DAY.end(), false));
    }
    return options;
  }

}
