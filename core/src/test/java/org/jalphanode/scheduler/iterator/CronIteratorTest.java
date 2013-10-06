/**
 *    Copyright 2011 Pedro Ribeiro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.jalphanode.scheduler.iterator;

import java.text.ParseException;

import org.jalphanode.scheduler.CronIterator;
import org.jalphanode.scheduler.SchedulerParseException;

import org.testng.annotations.Test;

/**
 * Cron iterator test.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public class CronIteratorTest {

    @Test
    public void cronIterator() throws SchedulerParseException, ParseException {

        new CronIterator("0 0 12 * * ?");

        new CronIterator("0 15 10 ? * *");

        new CronIterator("0 15 10 ? * MON-FRI");

        new CronIterator("0 0 0 25 12 ?");
    }
}
