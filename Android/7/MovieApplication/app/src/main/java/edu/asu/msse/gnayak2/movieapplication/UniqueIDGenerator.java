package edu.asu.msse.gnayak2.movieapplication;


import java.util.UUID;

/**
 * Copyright 2016 Gowtham Ganesh Nayak,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: Uses Java's Universally Unique Identifier class to generate
 * unique identifiers which can be consumed by other objects.
 *
 * SER598 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 *
 * @author Gowtham Ganesh Nayak mailto:gnayak2@asu.edu
 * @version March 2016
 */

public class UniqueIDGenerator {

    /**
     * Overridden method to creates a database table.
     *
     * @return Returns String. A unique string id.
     */
    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}
