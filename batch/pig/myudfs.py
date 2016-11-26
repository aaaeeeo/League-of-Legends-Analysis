#!/usr/bin/python

@outputSchema('concated: chararray')
def concat_bag(BAG):
    return ','.join([ i[0] for i in BAG ])