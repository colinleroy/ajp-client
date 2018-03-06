======================================
A client for the Apache JServ Protocol
======================================

I've often wanted a Java client for AJP_, but never found one.  So
while waiting for a maintenance window to roll around at work, I
finally wrote one.

Yes, I know the code is horrible; yes, I know there is no
documentation.  Both of those things can be fixed in time.

==================
Command-Line Usage
==================

``Usage: java -jar ajp-client.jar [OPTIONS]``

``--help                : this help``

``-h --host  [HOST]        : [required] hostname to connect to``

``-p --port  [PORT]        : [required] port to connect to``

``-u --url   [URL]         : [optional] URL to fetch``

``-l --login [LOGIN]       : [optional] Login``

``--password [PASSWORD] : [optional] Password``

``--post [FILE]         : [optional] File to post data from``

``--header [HEADER]     : [optional] Extra request header in the form "Header: Value". Multiple extra headers can be sent``


====================
 Boring legal stuff
====================

Copyright (c) 2010 Espen Wiborg <espenhw@grumblesmurf.org>
Copyright (c) 2018 Colin Leroy <colin@colino.net>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

.. _AJP: http://tomcat.apache.org/connectors-doc/ajp/ajpv13a.html
